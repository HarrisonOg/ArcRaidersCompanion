package com.harrisonog.arcraiderscompanion.ui.quest

import androidx.lifecycle.SavedStateHandle
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
class QuestDetailViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questId: String = checkNotNull(savedStateHandle["questId"])

    private val _uiState = MutableStateFlow(QuestDetailUiState())
    val uiState: StateFlow<QuestDetailUiState> = _uiState.asStateFlow()

    init {
        loadQuest()
    }

    fun onEvent(event: QuestDetailEvent) {
        when (event) {
            is QuestDetailEvent.UpdateStatus -> updateStatus(event.status)
            is QuestDetailEvent.ToggleObjective -> toggleObjective(event.objectiveId)
            is QuestDetailEvent.MarkAsInProgress -> updateStatus(QuestStatus.IN_PROGRESS)
            is QuestDetailEvent.MarkAsCompleted -> updateStatus(QuestStatus.COMPLETED)
        }
    }

    private fun loadQuest() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            questRepository.getQuestById(questId).collect { quest ->
                _uiState.update {
                    it.copy(
                        quest = quest,
                        isLoading = false,
                        error = if (quest == null) "Quest not found" else null
                    )
                }
            }
        }
    }

    private fun updateStatus(status: QuestStatus) {
        val currentQuest = _uiState.value.quest ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }

            when (val result = questRepository.updateQuestStatus(questId, status)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = result.message ?: "Failed to update quest status"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }

    private fun toggleObjective(objectiveId: String) {
        val currentQuest = _uiState.value.quest ?: return
        val completedObjectives = currentQuest.completedObjectives.toMutableSet()

        if (completedObjectives.contains(objectiveId)) {
            completedObjectives.remove(objectiveId)
        } else {
            completedObjectives.add(objectiveId)
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }

            when (val result = questRepository.updateCompletedObjectives(questId, completedObjectives)) {
                is Result.Success -> {
                    // Check if all objectives are completed
                    val allObjectivesCompleted = currentQuest.objectives.all { objective ->
                        completedObjectives.contains(objective.id)
                    }

                    // Auto-update status if all objectives completed
                    if (allObjectivesCompleted && currentQuest.status != QuestStatus.COMPLETED) {
                        questRepository.updateQuestStatus(questId, QuestStatus.COMPLETED)
                    } else if (completedObjectives.isNotEmpty() && currentQuest.status == QuestStatus.NOT_STARTED) {
                        questRepository.updateQuestStatus(questId, QuestStatus.IN_PROGRESS)
                    }

                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = result.message ?: "Failed to update objective"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }
}
