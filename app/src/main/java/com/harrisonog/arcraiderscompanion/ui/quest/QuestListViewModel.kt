package com.harrisonog.arcraiderscompanion.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.repository.QuestRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestListViewModel @Inject constructor(
    private val questRepository: QuestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestListUiState())
    val uiState: StateFlow<QuestListUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
        syncQuests()
    }

    fun onEvent(event: QuestListEvent) {
        when (event) {
            is QuestListEvent.RefreshQuests -> refreshQuests()
            is QuestListEvent.FilterByStatus -> filterByStatus(event.status)
            is QuestListEvent.NavigateToQuestDetail -> {
                // Navigation handled by UI
            }
        }
    }

    private fun loadQuests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val flow = _uiState.value.filterStatus?.let { status ->
                questRepository.getQuestsByStatus(status)
            } ?: questRepository.getAllQuests()

            flow.collect { quests ->
                _uiState.update {
                    it.copy(
                        quests = quests,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun syncQuests() {
        viewModelScope.launch {
            when (val result = questRepository.syncQuests()) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to sync quests"
                        )
                    }
                }
                is Result.Success -> {
                    // Successfully synced
                }
                Result.Loading -> {}
            }
        }
    }

    private fun refreshQuests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            when (val result = questRepository.refreshQuests()) {
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
                            error = result.message ?: "Failed to refresh quests"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }

    private fun filterByStatus(status: QuestStatus?) {
        _uiState.update { it.copy(filterStatus = status) }
        loadQuests()
    }
}
