package com.harrisonog.arcraiderscompanion.data.mapper

import com.harrisonog.arcraiderscompanion.data.remote.QuestDto
import com.harrisonog.arcraiderscompanion.data.remote.QuestItemDto
import com.harrisonog.arcraiderscompanion.domain.model.*

fun QuestDto.toDomain(): Quest? {
    val questId = id ?: return null
    val questName = name ?: return null

    return Quest(
        id = questId,
        name = questName,
        description = null, // API doesn't provide description
        objectives = objectives?.mapIndexed { index, description ->
            QuestObjective(
                id = "$questId-objective-$index",
                description = description,
                isCompleted = false,
                orderIndex = index
            )
        } ?: emptyList(),
        requiredItems = requiredItems?.mapNotNull { it.toRequiredItem() } ?: emptyList(),
        rewards = (rewards?.mapNotNull { it.toReward() } ?: emptyList()) +
                  (grantedItems?.mapNotNull { it.toReward() } ?: emptyList()),
        xpReward = xp,
        status = QuestStatus.NOT_STARTED,
        completedObjectives = emptySet(),
        mapLocation = locations?.firstOrNull()?.map,
        questChain = markerCategory,
        prerequisiteQuests = emptyList(), // API doesn't provide this
        imageUrl = image
    )
}

fun QuestItemDto.toRequiredItem(): RequiredItem? {
    val itemDetails = item ?: return null
    val itemIdValue = itemDetails.id ?: itemId ?: return null
    val itemNameValue = itemDetails.name ?: return null
    val quantityValue = quantity?.toIntOrNull() ?: 1

    return RequiredItem(
        itemId = itemIdValue,
        itemName = itemNameValue,
        quantity = quantityValue,
        imageUrl = itemDetails.icon
    )
}

fun QuestItemDto.toReward(): Reward? {
    val itemDetails = item ?: return null
    val itemNameValue = itemDetails.name ?: return null
    val quantityValue = quantity?.toIntOrNull() ?: 1

    return Reward(
        itemId = itemDetails.id,
        itemName = itemNameValue,
        quantity = quantityValue,
        imageUrl = itemDetails.icon,
        type = RewardType.ITEM
    )
}
