package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harrisonog.arcraiderscompanion.domain.model.*

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val category: ItemCategory,
    val rarity: ItemRarity,
    val isQuestItem: Boolean,
    val neededForQuests: List<String>,
    val sellValue: Int?,
    val recyclingMaterials: List<RecyclingMaterial>,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun ItemEntity.toDomain(): Item {
    return Item(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        category = category,
        rarity = rarity,
        isQuestItem = isQuestItem,
        neededForQuests = neededForQuests,
        sellValue = sellValue,
        recyclingMaterials = recyclingMaterials
    )
}

fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        category = category,
        rarity = rarity,
        isQuestItem = isQuestItem,
        neededForQuests = neededForQuests,
        sellValue = sellValue,
        recyclingMaterials = recyclingMaterials
    )
}
