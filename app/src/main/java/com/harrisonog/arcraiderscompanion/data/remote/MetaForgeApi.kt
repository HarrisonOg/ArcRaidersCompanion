package com.harrisonog.arcraiderscompanion.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * MetaForge API Service
 * Base URL: https://metaforge.app/api/arc-raiders
 * Documentation: https://metaforge.app/arc-raiders/api
 */
interface MetaForgeApi {

    /**
     * Get all quests
     * Endpoint: /api/arc-raiders/quests
     */
    @GET("quests")
    suspend fun getQuests(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): List<QuestDto>

    /**
     * Get all items
     * Endpoint: /api/arc-raiders/items
     */
    @GET("items")
    suspend fun getItems(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("category") category: String? = null,
        @Query("quest_items_only") questItemsOnly: Boolean? = null
    ): List<ItemDto>

    companion object {
        const val BASE_URL = "https://metaforge.app/api/arc-raiders/"
    }
}