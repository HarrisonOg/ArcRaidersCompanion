package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.*
import com.harrisonog.arcraiderscompanion.data.local.entity.WorkshopUpgradeEntity
import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkshopUpgradeDao {

    @Query("SELECT * FROM workshop_upgrades ORDER BY stationId ASC, levelNumber ASC")
    fun getAllUpgrades(): Flow<List<WorkshopUpgradeEntity>>

    @Query("SELECT * FROM workshop_upgrades WHERE levelId = :levelId")
    fun getUpgradeById(levelId: String): Flow<WorkshopUpgradeEntity?>

    @Query("SELECT * FROM workshop_upgrades WHERE stationId = :stationId ORDER BY levelNumber ASC")
    fun getUpgradesByStation(stationId: String): Flow<List<WorkshopUpgradeEntity>>

    @Query("SELECT * FROM workshop_upgrades WHERE status = :status ORDER BY stationId ASC, levelNumber ASC")
    fun getUpgradesByStatus(status: UpgradeStatus): Flow<List<WorkshopUpgradeEntity>>

    @Query("SELECT * FROM workshop_upgrades WHERE levelId = :levelId")
    suspend fun getUpgradeByIdSync(levelId: String): WorkshopUpgradeEntity?

    @Query("SELECT DISTINCT stationId FROM workshop_upgrades ORDER BY stationId ASC")
    fun getAllStationIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpgrade(upgrade: WorkshopUpgradeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpgrades(upgrades: List<WorkshopUpgradeEntity>)

    @Update
    suspend fun updateUpgrade(upgrade: WorkshopUpgradeEntity)

    @Query("UPDATE workshop_upgrades SET status = :status WHERE levelId = :levelId")
    suspend fun updateUpgradeStatus(levelId: String, status: UpgradeStatus)

    @Query("DELETE FROM workshop_upgrades")
    suspend fun deleteAllUpgrades()

    @Query("SELECT COUNT(*) FROM workshop_upgrades")
    suspend fun getUpgradeCount(): Int

    @Query("SELECT COUNT(*) FROM workshop_upgrades WHERE status = :status")
    suspend fun getUpgradeCountByStatus(status: UpgradeStatus): Int
}
