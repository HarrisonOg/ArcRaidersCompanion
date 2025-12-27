package com.harrisonog.arcraiderscompanion.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Objects for MetaForge API
 * API Documentation: https://metaforge.app/arc-raiders/api
 */

// Quest Response DTOs
data class QuestDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("objectives") val objectives: List<ObjectiveDto>?,
    @SerializedName("required_items") val requiredItems: List<RequiredItemDto>?,
    @SerializedName("rewards") val rewards: List<RewardDto>?,
    @SerializedName("xp") val xp: Int?,
    @SerializedName("map") val map: String?,
    @SerializedName("quest_chain") val questChain: String?,
    @SerializedName("prerequisites") val prerequisites: List<String>?,
    @SerializedName("image_url") val imageUrl: String?
)

data class ObjectiveDto(
    @SerializedName("id") val id: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("order") val order: Int?
)

data class RequiredItemDto(
    @SerializedName("item_id") val itemId: String?,
    @SerializedName("item_name") val itemName: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("image_url") val imageUrl: String?
)

data class RewardDto(
    @SerializedName("item_id") val itemId: String?,
    @SerializedName("item_name") val itemName: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("type") val type: String?
)

// Item Response DTOs
data class ItemDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("rarity") val rarity: String?,
    @SerializedName("is_quest_item") val isQuestItem: Boolean?,
    @SerializedName("needed_for_quests") val neededForQuests: List<String>?,
    @SerializedName("sell_value") val sellValue: Int?,
    @SerializedName("recycling_materials") val recyclingMaterials: List<RecyclingMaterialDto>?
)

data class RecyclingMaterialDto(
    @SerializedName("material_name") val materialName: String?,
    @SerializedName("quantity") val quantity: Int?
)

// API Response Wrappers
data class QuestsResponse(
    @SerializedName("quests") val quests: List<QuestDto>?,
    @SerializedName("total") val total: Int?,
    @SerializedName("page") val page: Int?,
    @SerializedName("per_page") val perPage: Int?
)

data class ItemsResponse(
    @SerializedName("items") val items: List<ItemDto>?,
    @SerializedName("total") val total: Int?,
    @SerializedName("page") val page: Int?,
    @SerializedName("per_page") val perPage: Int?
)