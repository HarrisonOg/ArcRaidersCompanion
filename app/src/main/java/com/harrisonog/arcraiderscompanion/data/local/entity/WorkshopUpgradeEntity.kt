package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harrisonog.arcraiderscompanion.domain.model.*

@Entity(tableName = "workshop_upgrades")
data class WorkshopUpgradeEntity(
    @PrimaryKey
    val levelId: String,
    val stationId: String,
    val levelNumber: Int,
    val name: String,
    val description: String?,
    val requiredItems: List<RequiredItem>,
    val rewards: List<Reward>,
    val unlocks: String?,
    val status: UpgradeStatus,
    val imageUrl: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun WorkshopUpgradeEntity.toDomain(): WorkshopUpgrade {
    return WorkshopUpgrade(
        levelId = levelId,
        stationId = stationId,
        levelNumber = levelNumber,
        name = name,
        description = description,
        requiredItems = requiredItems,
        rewards = rewards,
        unlocks = unlocks,
        status = status,
        imageUrl = imageUrl
    )
}

fun WorkshopUpgrade.toEntity(): WorkshopUpgradeEntity {
    return WorkshopUpgradeEntity(
        levelId = levelId,
        stationId = stationId,
        levelNumber = levelNumber,
        name = name,
        description = description,
        requiredItems = requiredItems,
        rewards = rewards,
        unlocks = unlocks,
        status = status,
        imageUrl = imageUrl
    )
}
