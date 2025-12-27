package com.harrisonog.arcraiderscompanion.ui.quest

import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.model.RequiredItemWithInventory

data class QuestDetailUiState(
    val quest: Quest? = null,
    val requiredItemsWithInventory: List<RequiredItemWithInventory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false
)

sealed class QuestDetailEvent {
    data class UpdateStatus(val status: QuestStatus) : QuestDetailEvent()
    data class ToggleObjective(val objectiveId: String) : QuestDetailEvent()
    data object MarkAsInProgress : QuestDetailEvent()
    data object MarkAsCompleted : QuestDetailEvent()
    data class UpdateItemQuantity(
        val itemId: String,
        val itemName: String,
        val quantity: Int,
        val imageUrl: String?
    ) : QuestDetailEvent()
}
