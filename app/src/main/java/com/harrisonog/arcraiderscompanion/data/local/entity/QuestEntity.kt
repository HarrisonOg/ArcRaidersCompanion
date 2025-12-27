package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harrisonog.arcraiderscompanion.domain.model.*

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val objectives: List<QuestObjective>,
    val requiredItems: List<RequiredItem>,
    val rewards: List<Reward>,
    val xpReward: Int?,
    val status: QuestStatus,
    val completedObjectives: Set<String>,
    val mapLocation: String?,
    val questChain: String?,
    val prerequisiteQuests: List<String>,
    val imageUrl: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun QuestEntity.toDomain(): Quest {
    return Quest(
        id = id,
        name = name,
        description = description,
        objectives = objectives,
        requiredItems = requiredItems,
        rewards = rewards,
        xpReward = xpReward,
        status = status,
        completedObjectives = completedObjectives,
        mapLocation = mapLocation,
        questChain = questChain,
        prerequisiteQuests = prerequisiteQuests,
        imageUrl = imageUrl
    )
}

fun Quest.toEntity(): QuestEntity {
    return QuestEntity(
        id = id,
        name = name,
        description = description,
        objectives = objectives,
        requiredItems = requiredItems,
        rewards = rewards,
        xpReward = xpReward,
        status = status,
        completedObjectives = completedObjectives,
        mapLocation = mapLocation,
        questChain = questChain,
        prerequisiteQuests = prerequisiteQuests,
        imageUrl = imageUrl
    )
}
