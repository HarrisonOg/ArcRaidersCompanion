package com.harrisonog.arcraiderscompanion.ui.mapevent

import com.harrisonog.arcraiderscompanion.domain.model.MapEvent

data class MapEventListUiState(
    val mapEventsGrouped: Map<String, List<MapEvent>> = emptyMap(),
    val expandedMaps: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class MapEventListEvent {
    data object RefreshMapEvents : MapEventListEvent()
    data class ToggleMapExpansion(val mapName: String) : MapEventListEvent()
}
