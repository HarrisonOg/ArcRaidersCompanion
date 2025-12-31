package com.harrisonog.arcraiderscompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a single upgrade level for a workshop station
 */
@Parcelize
data class WorkshopUpgrade(
    val levelId: String,
    val stationId: String,
    val levelNumber: Int,
    val name: String,
    val description: String?,
    val requiredItems: List<RequiredItem>,
    val rewards: List<Reward>,
    val unlocks: String?,
    val status: UpgradeStatus = UpgradeStatus.LOCKED,
    val imageUrl: String? = null
) : Parcelable {
    val isUnlocked: Boolean get() = status != UpgradeStatus.LOCKED
    val isCompleted: Boolean get() = status == UpgradeStatus.COMPLETED
}

/**
 * Represents a workshop station with all its upgrade levels
 */
@Parcelize
data class WorkshopStation(
    val stationId: String,
    val stationName: String,
    val description: String?,
    val imageUrl: String? = null,
    val levels: List<WorkshopUpgrade> = emptyList()
) : Parcelable {
    val currentLevel: Int get() = levels.count { it.status == UpgradeStatus.COMPLETED }
    val maxLevel: Int get() = levels.size
    val nextUpgrade: WorkshopUpgrade? get() = levels.firstOrNull {
        it.status == UpgradeStatus.NOT_STARTED || it.status == UpgradeStatus.IN_PROGRESS
    }
    val progress: Float get() = if (maxLevel > 0) currentLevel.toFloat() / maxLevel else 0f
}

/**
 * Status of a workshop upgrade
 */
enum class UpgradeStatus {
    LOCKED,        // Not yet available (previous level not completed)
    NOT_STARTED,   // Available but not started
    IN_PROGRESS,   // Currently working on it
    COMPLETED      // Finished
}

/**
 * Workshop upgrade with inventory tracking
 */
data class WorkshopUpgradeWithInventory(
    val upgrade: WorkshopUpgrade,
    val requiredItemsWithInventory: List<RequiredItemWithInventory>
) {
    val hasAllItems: Boolean get() = requiredItemsWithInventory.all { it.isComplete }
    val totalProgress: Float get() = if (requiredItemsWithInventory.isEmpty()) 0f
    else requiredItemsWithInventory.sumOf { it.percentComplete.toDouble() }.toFloat() / requiredItemsWithInventory.size
}
