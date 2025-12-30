package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.QuestDao
import com.harrisonog.arcraiderscompanion.data.local.entity.toDomain
import com.harrisonog.arcraiderscompanion.data.local.entity.toEntity
import com.harrisonog.arcraiderscompanion.data.local.preferences.SyncPreferences
import com.harrisonog.arcraiderscompanion.data.mapper.toDomain
import com.harrisonog.arcraiderscompanion.data.remote.MetaForgeApi
import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.repository.QuestRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestRepositoryImpl @Inject constructor(
    private val api: MetaForgeApi,
    private val questDao: QuestDao,
    private val syncPreferences: SyncPreferences
) : QuestRepository {

    override fun getAllQuests(): Flow<List<Quest>> {
        return questDao.getAllQuests().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestById(questId: String): Flow<Quest?> {
        return questDao.getQuestById(questId).map { it?.toDomain() }
    }

    override fun getQuestsByStatus(status: QuestStatus): Flow<List<Quest>> {
        return questDao.getQuestsByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestsByChain(questChain: String): Flow<List<Quest>> {
        return questDao.getQuestsByChain(questChain).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshQuests(): Result<Unit> {
        return try {
            val allQuests = mutableListOf<Quest>()
            var currentPage = 1
            var totalPages = 1

            // Fetch all pages
            do {
                val response = api.getQuests(page = currentPage, limit = 100)
                val questDtos = response.data ?: emptyList()
                val quests = questDtos.mapNotNull { it.toDomain() }
                allQuests.addAll(quests)

                // Update totalPages from pagination info
                totalPages = response.pagination?.totalPages ?: 1
                currentPage++
            } while (currentPage <= totalPages)

            // Process all quests, preserving local state
            allQuests.forEach { newQuest ->
                val existingQuest = questDao.getQuestByIdSync(newQuest.id)
                if (existingQuest != null) {
                    val updatedQuest = newQuest.copy(
                        status = existingQuest.status,
                        completedObjectives = existingQuest.completedObjectives
                    )
                    questDao.insertQuest(updatedQuest.toEntity())
                } else {
                    questDao.insertQuest(newQuest.toEntity())
                }
            }

            // Update last sync timestamp
            syncPreferences.setLastQuestSync(System.currentTimeMillis())

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateQuestStatus(questId: String, status: QuestStatus): Result<Unit> {
        return try {
            questDao.updateQuestStatus(questId, status)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCompletedObjectives(
        questId: String,
        completedObjectives: Set<String>
    ): Result<Unit> {
        return try {
            questDao.updateCompletedObjectives(questId, completedObjectives)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncQuests(): Result<Unit> {
        // Check if we should sync based on time or empty database
        val shouldSync = syncPreferences.shouldSyncQuests()
        val questCount = questDao.getQuestCount()

        return if (shouldSync || questCount == 0) {
            refreshQuests()
        } else {
            Result.Success(Unit)
        }
    }
}
