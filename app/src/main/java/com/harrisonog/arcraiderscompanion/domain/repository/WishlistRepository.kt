package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.RequiredItem
import com.harrisonog.arcraiderscompanion.domain.model.WishlistItem
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getAllWishlistItems(): Flow<List<WishlistItem>>

    fun getWishlistItem(itemId: String): Flow<WishlistItem?>

    suspend fun addToWishlist(
        itemId: String,
        itemName: String,
        quantity: Int,
        imageUrl: String?,
        isManual: Boolean,
        questId: String? = null
    ): Result<Unit>

    suspend fun removeFromWishlist(itemId: String): Result<Unit>

    suspend fun updateQuantityNeeded(itemId: String, quantity: Int): Result<Unit>

    suspend fun addQuestItemsToWishlist(questId: String, items: List<RequiredItem>): Result<Unit>

    suspend fun removeQuestItemsFromWishlist(questId: String): Result<Unit>

    suspend fun getWishlistItemCount(): Int
}
