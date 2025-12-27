package com.harrisonog.arcraiderscompanion.data.mapper

import com.harrisonog.arcraiderscompanion.data.remote.ItemDto
import com.harrisonog.arcraiderscompanion.data.remote.RecyclingMaterialDto
import com.harrisonog.arcraiderscompanion.domain.model.Item
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity
import com.harrisonog.arcraiderscompanion.domain.model.RecyclingMaterial

fun ItemDto.toDomain(): Item? {
    val itemId = id ?: return null
    val itemName = name ?: return null

    return Item(
        id = itemId,
        name = itemName,
        description = description,
        imageUrl = imageUrl,
        category = category.toItemCategory(),
        rarity = rarity.toItemRarity(),
        isQuestItem = isQuestItem ?: false,
        neededForQuests = neededForQuests ?: emptyList(),
        sellValue = sellValue,
        recyclingMaterials = recyclingMaterials?.mapNotNull { it.toDomain() } ?: emptyList()
    )
}

fun RecyclingMaterialDto.toDomain(): RecyclingMaterial? {
    val name = materialName ?: return null
    val qty = quantity ?: return null

    return RecyclingMaterial(
        materialName = name,
        quantity = qty
    )
}

private fun String?.toItemCategory(): ItemCategory {
    return when (this?.lowercase()?.replace(" ", "_")) {
        "weapon" -> ItemCategory.WEAPON
        "armor" -> ItemCategory.ARMOR
        "consumable" -> ItemCategory.CONSUMABLE
        "material" -> ItemCategory.MATERIAL
        "quest_item" -> ItemCategory.QUEST_ITEM
        "mod" -> ItemCategory.MOD
        "ammo" -> ItemCategory.AMMO
        "equipment" -> ItemCategory.EQUIPMENT
        "key" -> ItemCategory.KEY
        else -> ItemCategory.OTHER
    }
}

private fun String?.toItemRarity(): ItemRarity {
    return when (this?.lowercase()) {
        "common" -> ItemRarity.COMMON
        "uncommon" -> ItemRarity.UNCOMMON
        "rare" -> ItemRarity.RARE
        "epic" -> ItemRarity.EPIC
        "legendary" -> ItemRarity.LEGENDARY
        else -> ItemRarity.COMMON
    }
}
