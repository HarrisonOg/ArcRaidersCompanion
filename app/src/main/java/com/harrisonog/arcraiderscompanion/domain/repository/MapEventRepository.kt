package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.MapEvent
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MapEventRepository {
    fun getAllMapEvents(): Flow<List<MapEvent>>
    fun getEventsByMap(map: String): Flow<List<MapEvent>>
    suspend fun refreshMapEvents(): Result<Unit>
    suspend fun syncMapEvents(): Result<Unit>
}
