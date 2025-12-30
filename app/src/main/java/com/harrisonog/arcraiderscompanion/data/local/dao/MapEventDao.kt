package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harrisonog.arcraiderscompanion.data.local.entity.MapEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MapEventDao {

    @Query("SELECT * FROM map_events ORDER BY map ASC, name ASC")
    fun getAllMapEvents(): Flow<List<MapEventEntity>>

    @Query("SELECT * FROM map_events WHERE map = :map ORDER BY name ASC")
    fun getEventsByMap(map: String): Flow<List<MapEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapEvents(events: List<MapEventEntity>)

    @Query("DELETE FROM map_events")
    suspend fun deleteAllMapEvents()

    @Query("SELECT COUNT(*) FROM map_events")
    suspend fun getEventCount(): Int
}
