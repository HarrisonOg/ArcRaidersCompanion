package com.harrisonog.arcraiderscompanion.data.mapper

import com.harrisonog.arcraiderscompanion.data.local.EventDescriptionProvider
import com.harrisonog.arcraiderscompanion.data.remote.EventDto
import com.harrisonog.arcraiderscompanion.data.remote.EventTimeDto
import com.harrisonog.arcraiderscompanion.domain.model.EventTime
import com.harrisonog.arcraiderscompanion.domain.model.MapEvent

/**
 * Converts EventDto to MapEvent domain model
 * Returns null if required fields (name, map) are missing
 *
 * @param descriptionProvider Provider for local event descriptions from assets
 */
fun EventDto.toDomain(descriptionProvider: EventDescriptionProvider): MapEvent? {
    val eventName = name ?: return null
    val mapName = map ?: return null

    // Generate unique ID from map and event name
    val id = "${mapName}_${eventName}".replace(" ", "_").lowercase()

    // Convert time DTOs to domain models
    val eventTimes = times?.mapNotNull { it.toDomain() } ?: emptyList()

    return MapEvent(
        id = id,
        name = eventName,
        map = mapName,
        iconUrl = icon,
        description = description?.takeIf { it.isNotBlank() }
            ?: descriptionProvider.getDescription(eventName),
        times = eventTimes
    )
}

/**
 * Converts EventTimeDto to EventTime domain model
 * Returns null if start or end time is missing
 */
fun EventTimeDto.toDomain(): EventTime? {
    val startTime = start ?: return null
    val endTime = end ?: return null

    return EventTime(
        startTime = startTime,
        endTime = endTime
    )
}
