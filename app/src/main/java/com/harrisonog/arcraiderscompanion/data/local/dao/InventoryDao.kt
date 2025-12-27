package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.*
import com.harrisonog.arcraiderscompanion.data.local.entity.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory ORDER BY itemName ASC")
    fun getAllInventoryItems(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory WHERE itemId = :itemId")
    fun getInventoryItem(itemId: String): Flow<InventoryItemEntity?>

    @Query("SELECT * FROM inventory WHERE itemId = :itemId")
    suspend fun getInventoryItemSync(itemId: String): InventoryItemEntity?

    @Query("SELECT * FROM inventory WHERE itemId IN (:itemIds)")
    fun getInventoryItemsByIds(itemIds: List<String>): Flow<List<InventoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(item: InventoryItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItems(items: List<InventoryItemEntity>)

    @Update
    suspend fun updateInventoryItem(item: InventoryItemEntity)

    @Query("UPDATE inventory SET quantity = :quantity WHERE itemId = :itemId")
    suspend fun updateQuantity(itemId: String, quantity: Int)

    @Delete
    suspend fun deleteInventoryItem(item: InventoryItemEntity)

    @Query("DELETE FROM inventory")
    suspend fun deleteAllInventory()

    @Query("SELECT COUNT(*) FROM inventory WHERE quantity > 0")
    suspend fun getCollectedItemCount(): Int
}
