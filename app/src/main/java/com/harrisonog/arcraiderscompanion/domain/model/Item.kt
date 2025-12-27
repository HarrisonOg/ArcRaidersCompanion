package com.harrisonog.arcraiderscompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String? = null,
    val category: ItemCategory = ItemCategory.OTHER,
    val rarity: ItemRarity = ItemRarity.COMMON,
    val isQuestItem: Boolean = false,
    val neededForQuests: List<String> = emptyList(),
    val sellValue: Int? = null,
    val recyclingMaterials: List<RecyclingMaterial> = emptyList()
) : Parcelable

@Parcelize
data class RecyclingMaterial(
    val materialName: String,
    val quantity: Int
) : Parcelable

enum class ItemCategory {
    WEAPON,
    ARMOR,
    CONSUMABLE,
    MATERIAL,
    QUEST_ITEM,
    MOD,
    AMMO,
    EQUIPMENT,
    KEY,
    OTHER
}

enum class ItemRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}