package com.harrisonog.arcraiderscompanion.ui.mapevent

data class EventDetailUiState(
    val eventGroup: EventGroup? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
