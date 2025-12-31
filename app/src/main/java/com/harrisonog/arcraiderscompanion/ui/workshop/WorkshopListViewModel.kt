package com.harrisonog.arcraiderscompanion.ui.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.repository.WorkshopUpgradeRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkshopListViewModel @Inject constructor(
    private val workshopUpgradeRepository: WorkshopUpgradeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkshopListUiState())
    val uiState: StateFlow<WorkshopListUiState> = _uiState.asStateFlow()

    init {
        loadWorkshopStations()
        loadUpgradesFromJson()
    }

    fun onEvent(event: WorkshopListEvent) {
        when (event) {
            is WorkshopListEvent.NavigateToStationDetail -> {
                // Navigation handled by UI
            }
            is WorkshopListEvent.NavigateToUpgradeDetail -> {
                // Navigation handled by UI
            }
            is WorkshopListEvent.RefreshData -> {
                loadUpgradesFromJson()
            }
        }
    }

    private fun loadWorkshopStations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            workshopUpgradeRepository.getAllStations().collect { stations ->
                _uiState.update {
                    it.copy(
                        stations = stations.sortedBy { station -> station.stationId },
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun loadUpgradesFromJson() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            when (val result = workshopUpgradeRepository.loadUpgradesFromJson()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message ?: "Failed to load workshop data"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }
}
