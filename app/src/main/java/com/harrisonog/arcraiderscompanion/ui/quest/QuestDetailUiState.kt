package com.harrisonog.arcraiderscompanion.ui.quest

import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus

data class QuestDetailUiState(
    val quest: Quest? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false
)

sealed class QuestDetailEvent {
    data class UpdateStatus(val status: QuestStatus) : QuestDetailEvent()
    data class ToggleObjective(val objectiveId: String) : QuestDetailEvent()
    data object MarkAsInProgress : QuestDetailEvent()
    data object MarkAsCompleted : QuestDetailEvent()
}
