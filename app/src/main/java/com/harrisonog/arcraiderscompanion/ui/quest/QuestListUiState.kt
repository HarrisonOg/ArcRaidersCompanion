package com.harrisonog.arcraiderscompanion.ui.quest

import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus

data class QuestListUiState(
    val quests: List<Quest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterStatus: QuestStatus? = null,
    val isRefreshing: Boolean = false
)

sealed class QuestListEvent {
    data object RefreshQuests : QuestListEvent()
    data class FilterByStatus(val status: QuestStatus?) : QuestListEvent()
    data class NavigateToQuestDetail(val questId: String) : QuestListEvent()
}
