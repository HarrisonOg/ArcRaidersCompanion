package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harrisonog.arcraiderscompanion.domain.model.EventTime
import com.harrisonog.arcraiderscompanion.domain.model.MapEvent

@Entity(tableName = "map_events")
data class MapEventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val map: String,
    val iconUrl: String?,
    val description: String?,
    val times: List<EventTime>,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun MapEventEntity.toDomain(): MapEvent {
    return MapEvent(
        id = id,
        name = name,
        map = map,
        iconUrl = iconUrl,
        description = description,
        times = times
    )
}

fun MapEvent.toEntity(): MapEventEntity {
    return MapEventEntity(
        id = id,
        name = name,
        map = map,
        iconUrl = iconUrl,
        description = description,
        times = times
    )
}
