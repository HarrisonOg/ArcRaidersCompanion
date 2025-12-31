package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.WorkshopLevelsProvider
import com.harrisonog.arcraiderscompanion.data.local.dao.WorkshopUpgradeDao
import com.harrisonog.arcraiderscompanion.data.local.entity.toDomain
import com.harrisonog.arcraiderscompanion.data.local.entity.toEntity
import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopStation
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopUpgrade
import com.harrisonog.arcraiderscompanion.domain.repository.WorkshopUpgradeRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkshopUpgradeRepositoryImpl @Inject constructor(
    private val workshopUpgradeDao: WorkshopUpgradeDao,
    private val workshopLevelsProvider: WorkshopLevelsProvider
) : WorkshopUpgradeRepository {

    override fun getAllUpgrades(): Flow<List<WorkshopUpgrade>> {
        return workshopUpgradeDao.getAllUpgrades().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUpgradeById(levelId: String): Flow<WorkshopUpgrade?> {
        return workshopUpgradeDao.getUpgradeById(levelId).map { it?.toDomain() }
    }

    override fun getUpgradesByStation(stationId: String): Flow<List<WorkshopUpgrade>> {
        return workshopUpgradeDao.getUpgradesByStation(stationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllStations(): Flow<List<WorkshopStation>> {
        return combine(
            workshopUpgradeDao.getAllStationIds(),
            workshopUpgradeDao.getAllUpgrades()
        ) { stationIds, upgradeEntities ->
            val upgrades = upgradeEntities.map { it.toDomain() }
            val upgradesByStation = upgrades.groupBy { it.stationId }

            stationIds.mapNotNull { stationId ->
                val metadata = workshopLevelsProvider.getStationMetadata(stationId)
                metadata?.let {
                    WorkshopStation(
                        stationId = it.stationId,
                        stationName = it.stationName,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        levels = upgradesByStation[stationId]?.sortedBy { upgrade -> upgrade.levelNumber } ?: emptyList()
                    )
                }
            }
        }
    }

    override fun getStationById(stationId: String): Flow<WorkshopStation?> {
        return workshopUpgradeDao.getUpgradesByStation(stationId).map { entities ->
            val upgrades = entities.map { it.toDomain() }
            val metadata = workshopLevelsProvider.getStationMetadata(stationId)

            metadata?.let {
                WorkshopStation(
                    stationId = it.stationId,
                    stationName = it.stationName,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    levels = upgrades.sortedBy { upgrade -> upgrade.levelNumber }
                )
            }
        }
    }

    override fun getUpgradesByStatus(status: UpgradeStatus): Flow<List<WorkshopUpgrade>> {
        return workshopUpgradeDao.getUpgradesByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun loadUpgradesFromJson(): Result<Unit> {
        return try {
            val jsonUpgrades = workshopLevelsProvider.getAllUpgrades()

            // Initialize upgrades with proper status based on sequential logic
            val processedUpgrades = mutableListOf<WorkshopUpgrade>()
            val upgradesByStation = jsonUpgrades.groupBy { it.stationId }

            upgradesByStation.forEach { (stationId, stationUpgrades) ->
                val sortedLevels = stationUpgrades.sortedBy { it.levelNumber }

                // Get existing upgrades for this station
                val existingUpgrades = try {
                    workshopUpgradeDao.getUpgradesByStation(stationId)
                        .first()
                        .map { it.toDomain() }
                } catch (e: Exception) {
                    emptyList()
                }
                val existingByLevel = existingUpgrades.associateBy { it.levelNumber }

                // Find highest completed level
                val highestCompleted = existingByLevel.values
                    .filter { it.status == UpgradeStatus.COMPLETED }
                    .maxByOrNull { it.levelNumber }

                sortedLevels.forEachIndexed { index, upgrade ->
                    val levelNumber = index + 1
                    val existing = existingByLevel[levelNumber]

                    val status = when {
                        existing != null -> existing.status  // Preserve existing status
                        levelNumber == 1 -> UpgradeStatus.NOT_STARTED  // Level 1 always unlocked
                        highestCompleted != null && levelNumber == highestCompleted.levelNumber + 1 ->
                            UpgradeStatus.NOT_STARTED  // Unlock next level after completed
                        else -> UpgradeStatus.LOCKED  // All others locked
                    }

                    processedUpgrades.add(upgrade.copy(status = status))
                }
            }

            workshopUpgradeDao.insertUpgrades(processedUpgrades.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUpgradeStatus(levelId: String, status: UpgradeStatus): Result<Unit> {
        return try {
            workshopUpgradeDao.updateUpgradeStatus(levelId, status)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun completeUpgrade(levelId: String): Result<Unit> {
        return try {
            val upgradeEntity = workshopUpgradeDao.getUpgradeByIdSync(levelId)
                ?: return Result.Error(Exception("Upgrade not found"))

            val upgrade = upgradeEntity.toDomain()

            // Mark current level as COMPLETED
            workshopUpgradeDao.updateUpgradeStatus(levelId, UpgradeStatus.COMPLETED)

            // Find and unlock next level
            val stationUpgrades = workshopUpgradeDao.getUpgradesByStation(upgrade.stationId)
                .first()
                .map { it.toDomain() }
                .sortedBy { it.levelNumber }

            // Find next level (current level + 1)
            val nextLevel = stationUpgrades.firstOrNull {
                it.levelNumber == upgrade.levelNumber + 1
            }

            // Unlock next level if it exists and is currently LOCKED
            if (nextLevel != null && nextLevel.status == UpgradeStatus.LOCKED) {
                workshopUpgradeDao.updateUpgradeStatus(
                    nextLevel.levelId,
                    UpgradeStatus.NOT_STARTED
                )
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun startUpgrade(levelId: String): Result<Unit> {
        return try {
            val upgradeEntity = workshopUpgradeDao.getUpgradeByIdSync(levelId)
                ?: return Result.Error(Exception("Upgrade not found"))

            val upgrade = upgradeEntity.toDomain()

            // Can only start if NOT_STARTED (not LOCKED)
            if (upgrade.status == UpgradeStatus.LOCKED) {
                return Result.Error(Exception("Cannot start locked upgrade. Complete previous level first."))
            }

            workshopUpgradeDao.updateUpgradeStatus(levelId, UpgradeStatus.IN_PROGRESS)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
