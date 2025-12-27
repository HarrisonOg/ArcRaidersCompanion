package com.harrisonog.arcraiderscompanion.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Objects for MetaForge API
 * API Documentation: https://metaforge.app/arc-raiders/api
 */

// API Response Wrappers
data class QuestsResponse(
    @SerializedName("data") val data: List<QuestDto>?,
    @SerializedName("pagination") val pagination: PaginationDto?
)

data class ItemsResponse(
    @SerializedName("data") val data: List<ItemDto>?,
    @SerializedName("pagination") val pagination: PaginationDto?,
    @SerializedName("maxValue") val maxValue: Int?
)

data class PaginationDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("total") val total: Int?,
    @SerializedName("totalPages") val totalPages: Int?,
    @SerializedName("hasNextPage") val hasNextPage: Boolean?,
    @SerializedName("hasPrevPage") val hasPrevPage: Boolean?
)

// Quest DTOs
data class QuestDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("objectives") val objectives: List<String>?,
    @SerializedName("xp") val xp: Int?,
    @SerializedName("granted_items") val grantedItems: List<QuestItemDto>?,
    @SerializedName("required_items") val requiredItems: List<QuestItemDto>?,
    @SerializedName("rewards") val rewards: List<QuestItemDto>?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("locations") val locations: List<LocationDto>?,
    @SerializedName("marker_category") val markerCategory: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("guide_links") val guideLinks: List<GuideLinkDto>?
)

data class QuestItemDto(
    @SerializedName("id") val id: String?,
    @SerializedName("item") val item: ItemDetailsDto?,
    @SerializedName("item_id") val itemId: String?,
    @SerializedName("quantity") val quantity: String?
)

data class ItemDetailsDto(
    @SerializedName("id") val id: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("rarity") val rarity: String?,
    @SerializedName("item_type") val itemType: String?
)

data class LocationDto(
    @SerializedName("x") val x: Double?,
    @SerializedName("y") val y: Double?,
    @SerializedName("map") val map: String?,
    @SerializedName("id") val id: String?
)

data class GuideLinkDto(
    @SerializedName("url") val url: String?,
    @SerializedName("label") val label: String?
)

// Item DTOs
data class ItemDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("item_type") val itemType: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("rarity") val rarity: String?,
    @SerializedName("value") val value: Int?,
    @SerializedName("loadout_slots") val loadoutSlots: List<String>?,
    @SerializedName("flavor_text") val flavorText: String?,
    @SerializedName("subcategory") val subcategory: String?,
    @SerializedName("workbench") val workbench: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
