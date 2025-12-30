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
        @Query("limit") limit: Int? = null
    ): QuestsResponse

    /**
     * Get all items
     * Endpoint: /api/arc-raiders/items
     */
    @GET("items")
    suspend fun getItems(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("item_type") itemType: String? = null
    ): ItemsResponse

    /**
     * Get event timers
     * Endpoint: /api/arc-raiders/event-timers
     */
    @GET("event-timers")
    suspend fun getEventTimers(): EventTimersResponse

    companion object {
        const val BASE_URL = "https://metaforge.app/api/arc-raiders/"
    }
}
