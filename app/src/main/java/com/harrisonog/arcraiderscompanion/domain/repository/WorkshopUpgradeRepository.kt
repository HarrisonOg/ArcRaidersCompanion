package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopStation
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopUpgrade
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface WorkshopUpgradeRepository {

    fun getAllUpgrades(): Flow<List<WorkshopUpgrade>>

    fun getUpgradeById(levelId: String): Flow<WorkshopUpgrade?>

    fun getUpgradesByStation(stationId: String): Flow<List<WorkshopUpgrade>>

    fun getAllStations(): Flow<List<WorkshopStation>>

    fun getStationById(stationId: String): Flow<WorkshopStation?>

    fun getUpgradesByStatus(status: UpgradeStatus): Flow<List<WorkshopUpgrade>>

    suspend fun loadUpgradesFromJson(): Result<Unit>

    suspend fun updateUpgradeStatus(levelId: String, status: UpgradeStatus): Result<Unit>

    suspend fun completeUpgrade(levelId: String): Result<Unit>

    suspend fun startUpgrade(levelId: String): Result<Unit>
}
