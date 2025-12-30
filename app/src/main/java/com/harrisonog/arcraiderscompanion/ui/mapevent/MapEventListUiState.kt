package com.harrisonog.arcraiderscompanion.ui.mapevent

import com.harrisonog.arcraiderscompanion.domain.model.EventTime
import com.harrisonog.arcraiderscompanion.domain.model.MapEvent

data class MapEventListUiState(
    val events: List<EventGroup> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

data class EventGroup(
    val id: String,
    val name: String,
    val description: String?,
    val iconUrl: String?,
    val mapOccurrences: List<MapOccurrence>,
    val isUpcoming: Boolean
)

data class MapOccurrence(
    val mapName: String,
    val times: List<EventTime>
)

sealed class MapEventListEvent {
    data object RefreshMapEvents : MapEventListEvent()
}
