package com.harrisonog.arcraiderscompanion.ui.mapevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.repository.MapEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mapEventRepository: MapEventRepository
) : ViewModel() {

    private val eventId: String = checkNotNull(savedStateHandle["eventId"])

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    init {
        loadEventDetail()
    }

    private fun loadEventDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            mapEventRepository.getAllMapEvents().collect { mapEvents ->
                // First, find the specific event by ID to get its name
                val targetEvent = mapEvents.find { it.id == eventId }

                if (targetEvent != null) {
                    // Now find ALL events with the same name across all maps
                    val allEventsWithSameName = mapEvents.filter { it.name == targetEvent.name }

                    val mapOccurrences = allEventsWithSameName.map { event ->
                        MapOccurrence(
                            mapName = event.map,
                            times = event.times
                        )
                    }

                    // Check if any time is upcoming
                    val isUpcoming = allEventsWithSameName.any { event ->
                        event.times.any { it.isUpcoming() }
                    }

                    val eventGroup = EventGroup(
                        id = targetEvent.id,
                        name = targetEvent.name,
                        description = targetEvent.description,
                        iconUrl = targetEvent.iconUrl,
                        mapOccurrences = mapOccurrences,
                        isUpcoming = isUpcoming
                    )

                    _uiState.update {
                        it.copy(
                            eventGroup = eventGroup,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Event not found"
                        )
                    }
                }
            }
        }
    }
}
