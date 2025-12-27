package com.harrisonog.arcraiderscompanion.data.mapper

import com.harrisonog.arcraiderscompanion.data.remote.ItemDto
import com.harrisonog.arcraiderscompanion.domain.model.Item
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity

fun ItemDto.toDomain(): Item? {
    val itemId = id ?: return null
    val itemName = name ?: return null

    return Item(
        id = itemId,
        name = itemName,
        description = description ?: flavorText,
        imageUrl = icon,
        category = itemType.toItemCategory(),
        rarity = rarity.toItemRarity(),
        isQuestItem = false, // API doesn't provide this
        neededForQuests = emptyList(), // API doesn't provide this
        sellValue = value,
        recyclingMaterials = emptyList() // API doesn't provide this
    )
}

private fun String?.toItemCategory(): ItemCategory {
    return when (this?.lowercase()?.replace(" ", "_")?.replace("-", "_")) {
        "weapon", "pistol", "shotgun", "smg", "assault_rifle", "marksman_rifle", "sniper_rifle", "launcher" -> ItemCategory.WEAPON
        "armor", "helmet", "chest", "legs" -> ItemCategory.ARMOR
        "consumable", "healing", "booster" -> ItemCategory.CONSUMABLE
        "material", "crafting_material", "component" -> ItemCategory.MATERIAL
        "quest_item" -> ItemCategory.QUEST_ITEM
        "mod", "weapon_mod", "armor_mod" -> ItemCategory.MOD
        "ammo", "ammunition" -> ItemCategory.AMMO
        "equipment", "gadget", "grenade", "deployable" -> ItemCategory.EQUIPMENT
        "key", "keycard" -> ItemCategory.KEY
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
