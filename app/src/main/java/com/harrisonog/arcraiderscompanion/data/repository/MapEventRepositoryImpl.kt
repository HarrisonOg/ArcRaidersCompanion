package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.MapEventDao
import com.harrisonog.arcraiderscompanion.data.local.entity.toDomain
import com.harrisonog.arcraiderscompanion.data.local.entity.toEntity
import com.harrisonog.arcraiderscompanion.data.local.preferences.SyncPreferences
import com.harrisonog.arcraiderscompanion.data.mapper.toDomain
import com.harrisonog.arcraiderscompanion.data.remote.MetaForgeApi
import com.harrisonog.arcraiderscompanion.domain.model.MapEvent
import com.harrisonog.arcraiderscompanion.domain.repository.MapEventRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapEventRepositoryImpl @Inject constructor(
    private val api: MetaForgeApi,
    private val mapEventDao: MapEventDao,
    private val syncPreferences: SyncPreferences
) : MapEventRepository {

    override fun getAllMapEvents(): Flow<List<MapEvent>> {
        return mapEventDao.getAllMapEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getEventsByMap(map: String): Flow<List<MapEvent>> {
        return mapEventDao.getEventsByMap(map).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshMapEvents(): Result<Unit> {
        return try {
            val response = api.getEventTimers()
            val eventDtos = response.data ?: emptyList()
            val events = eventDtos.mapNotNull { it.toDomain() }

            // Clear existing data and insert new data
            mapEventDao.deleteAllMapEvents()
            mapEventDao.insertMapEvents(events.map { it.toEntity() })

            // Update last sync timestamp
            syncPreferences.setLastMapEventSync(System.currentTimeMillis())

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncMapEvents(): Result<Unit> {
        // Check if we should sync based on time or empty database
        val shouldSync = syncPreferences.shouldSyncMapEvents()
        val eventCount = mapEventDao.getEventCount()

        return if (shouldSync || eventCount == 0) {
            refreshMapEvents()
        } else {
            Result.Success(Unit)
        }
    }
}
