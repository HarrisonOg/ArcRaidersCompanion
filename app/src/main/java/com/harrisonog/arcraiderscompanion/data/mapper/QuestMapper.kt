package com.harrisonog.arcraiderscompanion.data.mapper

import com.harrisonog.arcraiderscompanion.data.remote.ObjectiveDto
import com.harrisonog.arcraiderscompanion.data.remote.QuestDto
import com.harrisonog.arcraiderscompanion.data.remote.RequiredItemDto
import com.harrisonog.arcraiderscompanion.data.remote.RewardDto
import com.harrisonog.arcraiderscompanion.domain.model.*

fun QuestDto.toDomain(): Quest? {
    val questId = id ?: return null
    val questName = name ?: return null

    return Quest(
        id = questId,
        name = questName,
        description = description,
        objectives = objectives?.mapNotNull { it.toDomain() } ?: emptyList(),
        requiredItems = requiredItems?.mapNotNull { it.toDomain() } ?: emptyList(),
        rewards = rewards?.mapNotNull { it.toDomain() } ?: emptyList(),
        xpReward = xp,
        status = QuestStatus.NOT_STARTED,
        completedObjectives = emptySet(),
        mapLocation = map,
        questChain = questChain,
        prerequisiteQuests = prerequisites ?: emptyList(),
        imageUrl = imageUrl
    )
}

fun ObjectiveDto.toDomain(): QuestObjective? {
    val objectiveId = id ?: return null
    val objectiveDescription = description ?: return null

    return QuestObjective(
        id = objectiveId,
        description = objectiveDescription,
        isCompleted = false,
        orderIndex = order ?: 0
    )
}

fun RequiredItemDto.toDomain(): RequiredItem? {
    val id = itemId ?: return null
    val name = itemName ?: return null
    val qty = quantity ?: return null

    return RequiredItem(
        itemId = id,
        itemName = name,
        quantity = qty,
        imageUrl = imageUrl
    )
}

fun RewardDto.toDomain(): Reward? {
    val name = itemName ?: return null
    val qty = quantity ?: 1

    return Reward(
        itemId = itemId,
        itemName = name,
        quantity = qty,
        imageUrl = imageUrl,
        type = when (type?.lowercase()) {
            "item" -> RewardType.ITEM
            "currency" -> RewardType.CURRENCY
            "xp" -> RewardType.XP
            "unlock" -> RewardType.UNLOCK
            else -> RewardType.ITEM
        }
    )
}
