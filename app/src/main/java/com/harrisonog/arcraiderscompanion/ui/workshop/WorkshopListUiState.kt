package com.harrisonog.arcraiderscompanion.ui.workshop

import com.harrisonog.arcraiderscompanion.domain.model.WorkshopStation

data class WorkshopListUiState(
    val stations: List<WorkshopStation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class WorkshopListEvent {
    data class NavigateToStationDetail(val stationId: String) : WorkshopListEvent()
    data class NavigateToUpgradeDetail(val levelId: String) : WorkshopListEvent()
    data object RefreshData : WorkshopListEvent()
}
