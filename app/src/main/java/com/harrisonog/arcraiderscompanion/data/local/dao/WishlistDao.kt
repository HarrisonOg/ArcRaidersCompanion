package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.*
import com.harrisonog.arcraiderscompanion.data.local.entity.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist ORDER BY dateAdded DESC")
    fun getAllWishlistItems(): Flow<List<WishlistItemEntity>>

    @Query("SELECT * FROM wishlist WHERE itemId = :itemId")
    fun getWishlistItem(itemId: String): Flow<WishlistItemEntity?>

    @Query("SELECT * FROM wishlist WHERE itemId = :itemId")
    suspend fun getWishlistItemSync(itemId: String): WishlistItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItems(items: List<WishlistItemEntity>)

    @Update
    suspend fun updateWishlistItem(item: WishlistItemEntity)

    @Query("UPDATE wishlist SET quantityNeeded = :quantity, lastUpdated = :lastUpdated WHERE itemId = :itemId")
    suspend fun updateQuantity(itemId: String, quantity: Int, lastUpdated: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteWishlistItem(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist WHERE itemId = :itemId")
    suspend fun deleteWishlistItemById(itemId: String)

    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()

    @Query("SELECT COUNT(*) FROM wishlist")
    suspend fun getWishlistItemCount(): Int
}
