package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.InventoryItem
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getAllInventoryItems(): Flow<List<InventoryItem>>

    fun getInventoryItem(itemId: String): Flow<InventoryItem?>

    fun getInventoryItemsByIds(itemIds: List<String>): Flow<List<InventoryItem>>

    suspend fun updateItemQuantity(itemId: String, itemName: String, quantity: Int, imageUrl: String? = null): Result<Unit>

    suspend fun incrementItemQuantity(itemId: String, itemName: String, imageUrl: String? = null): Result<Unit>

    suspend fun decrementItemQuantity(itemId: String): Result<Unit>

    suspend fun getCollectedItemCount(): Int
}
