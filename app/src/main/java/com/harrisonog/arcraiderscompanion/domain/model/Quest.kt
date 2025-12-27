package com.harrisonog.arcraiderscompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quest(
    val id: String,
    val name: String,
    val description: String?,
    val objectives: List<QuestObjective>,
    val requiredItems: List<RequiredItem>,
    val rewards: List<Reward>,
    val xpReward: Int?,
    val status: QuestStatus = QuestStatus.NOT_STARTED,
    val completedObjectives: Set<String> = emptySet(),
    val mapLocation: String? = null,
    val questChain: String? = null,
    val prerequisiteQuests: List<String> = emptyList(),
    val imageUrl: String? = null
) : Parcelable

@Parcelize
data class QuestObjective(
    val id: String,
    val description: String,
    val isCompleted: Boolean = false,
    val orderIndex: Int = 0
) : Parcelable

@Parcelize
data class RequiredItem(
    val itemId: String,
    val itemName: String,
    val quantity: Int,
    val imageUrl: String? = null
) : Parcelable

@Parcelize
data class Reward(
    val itemId: String?,
    val itemName: String,
    val quantity: Int = 1,
    val imageUrl: String? = null,
    val type: RewardType = RewardType.ITEM
) : Parcelable

enum class RewardType {
    ITEM,
    CURRENCY,
    XP,
    UNLOCK
}

enum class QuestStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}
