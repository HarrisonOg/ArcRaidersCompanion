package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.*
import com.harrisonog.arcraiderscompanion.data.local.entity.ItemEntity
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :itemId")
    fun getItemById(itemId: String): Flow<ItemEntity?>

    @Query("SELECT * FROM items WHERE category = :category ORDER BY name ASC")
    fun getItemsByCategory(category: ItemCategory): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE rarity = :rarity ORDER BY name ASC")
    fun getItemsByRarity(rarity: ItemRarity): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE isQuestItem = 1 ORDER BY name ASC")
    fun getQuestItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id IN (:itemIds)")
    fun getItemsByIds(itemIds: List<String>): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemByIdSync(itemId: String): ItemEntity?

    @Query("SELECT * FROM items WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchItems(searchQuery: String): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("DELETE FROM items")
    suspend fun deleteAllItems()

    @Query("SELECT COUNT(*) FROM items")
    suspend fun getItemCount(): Int

    @Query("SELECT COUNT(*) FROM items WHERE category = :category")
    suspend fun getItemCountByCategory(category: ItemCategory): Int
}
