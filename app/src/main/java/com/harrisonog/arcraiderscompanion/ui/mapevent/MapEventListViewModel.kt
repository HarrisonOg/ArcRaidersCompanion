package com.harrisonog.arcraiderscompanion.ui.mapevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.repository.MapEventRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapEventListViewModel @Inject constructor(
    private val mapEventRepository: MapEventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapEventListUiState())
    val uiState: StateFlow<MapEventListUiState> = _uiState.asStateFlow()

    init {
        loadMapEvents()
        syncMapEvents()
    }

    fun onEvent(event: MapEventListEvent) {
        when (event) {
            is MapEventListEvent.RefreshMapEvents -> refreshMapEvents()
        }
    }

    private fun loadMapEvents() {
        viewModelScope.launch {
            mapEventRepository.getAllMapEvents().collect { mapEvents ->
                // Group by event name
                val grouped = mapEvents.groupBy { it.name }

                // Transform into EventGroup objects
                val eventGroups = grouped.map { (eventName, events) ->
                    val firstEvent = events.first()
                    val mapOccurrences = events.map { event ->
                        MapOccurrence(
                            mapName = event.map,
                            times = event.times
                        )
                    }

                    // Check if any time in any map occurrence is upcoming
                    val isUpcoming = events.any { event ->
                        event.times.any { it.isUpcoming() }
                    }

                    EventGroup(
                        id = firstEvent.id,
                        name = eventName,
                        description = firstEvent.description,
                        iconUrl = firstEvent.iconUrl,
                        mapOccurrences = mapOccurrences,
                        isUpcoming = isUpcoming
                    )
                }.sortedBy { it.name }

                _uiState.update { it.copy(events = eventGroups) }
            }
        }
    }

    private fun syncMapEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = mapEventRepository.syncMapEvents()) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to sync map events"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    private fun refreshMapEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            when (val result = mapEventRepository.refreshMapEvents()) {
                is Result.Success -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.exception.message ?: "Failed to refresh map events"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }
}
