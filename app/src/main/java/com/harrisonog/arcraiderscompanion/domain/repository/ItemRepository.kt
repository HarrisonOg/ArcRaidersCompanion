package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.Item
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<List<Item>>

    fun getItemById(itemId: String): Flow<Item?>

    fun getItemsByCategory(category: ItemCategory): Flow<List<Item>>

    fun getItemsByRarity(rarity: ItemRarity): Flow<List<Item>>

    fun getQuestItems(): Flow<List<Item>>

    fun getItemsByIds(itemIds: List<String>): Flow<List<Item>>

    fun searchItems(query: String): Flow<List<Item>>

    suspend fun refreshItems(): Result<Unit>

    suspend fun syncItems(): Result<Unit>
}
