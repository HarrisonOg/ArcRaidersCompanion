package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.WishlistDao
import com.harrisonog.arcraiderscompanion.data.local.entity.WishlistItemEntity
import com.harrisonog.arcraiderscompanion.domain.model.RequiredItem
import com.harrisonog.arcraiderscompanion.domain.model.WishlistItem
import com.harrisonog.arcraiderscompanion.domain.repository.WishlistRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getAllWishlistItems(): Flow<List<WishlistItem>> {
        return wishlistDao.getAllWishlistItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getWishlistItem(itemId: String): Flow<WishlistItem?> {
        return wishlistDao.getWishlistItem(itemId).map { it?.toDomain() }
    }

    override suspend fun addToWishlist(
        itemId: String,
        itemName: String,
        quantity: Int,
        imageUrl: String?,
        isManual: Boolean,
        questId: String?
    ): Result<Unit> {
        return try {
            val existing = wishlistDao.getWishlistItemSync(itemId)
            if (existing != null) {
                // Merge: update quantity and add questId if new
                val updatedQuestIds = if (questId != null && !existing.relatedQuestIds.contains(questId)) {
                    existing.relatedQuestIds + questId
                } else {
                    existing.relatedQuestIds
                }

                val updatedQuantity = existing.quantityNeeded + quantity

                wishlistDao.updateWishlistItem(
                    existing.copy(
                        quantityNeeded = updatedQuantity,
                        relatedQuestIds = updatedQuestIds,
                        isManual = existing.isManual || isManual,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            } else {
                wishlistDao.insertWishlistItem(
                    WishlistItemEntity(
                        itemId = itemId,
                        itemName = itemName,
                        quantityNeeded = quantity,
                        imageUrl = imageUrl,
                        isManual = isManual,
                        relatedQuestIds = questId?.let { listOf(it) } ?: emptyList()
                    )
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeFromWishlist(itemId: String): Result<Unit> {
        return try {
            wishlistDao.deleteWishlistItemById(itemId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateQuantityNeeded(itemId: String, quantity: Int): Result<Unit> {
        return try {
            wishlistDao.updateQuantity(itemId, quantity.coerceAtLeast(0))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addQuestItemsToWishlist(questId: String, items: List<RequiredItem>): Result<Unit> {
        return try {
            items.forEach { requiredItem ->
                addToWishlist(
                    itemId = requiredItem.itemId,
                    itemName = requiredItem.itemName,
                    quantity = requiredItem.quantity,
                    imageUrl = requiredItem.imageUrl,
                    isManual = false,
                    questId = questId
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeQuestItemsFromWishlist(questId: String): Result<Unit> {
        return try {
            val allWishlist = wishlistDao.getAllWishlistItems().first()

            allWishlist.forEach { item ->
                if (item.relatedQuestIds.contains(questId)) {
                    val updatedQuestIds = item.relatedQuestIds - questId

                    // If no more quests need it AND it wasn't manually added, remove
                    if (updatedQuestIds.isEmpty() && !item.isManual) {
                        wishlistDao.deleteWishlistItem(item)
                    } else {
                        // Keep the item but remove the quest reference
                        wishlistDao.updateWishlistItem(
                            item.copy(
                                relatedQuestIds = updatedQuestIds,
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getWishlistItemCount(): Int {
        return wishlistDao.getWishlistItemCount()
    }

    private fun WishlistItemEntity.toDomain(): WishlistItem {
        return WishlistItem(
            itemId = itemId,
            itemName = itemName,
            quantityNeeded = quantityNeeded,
            imageUrl = imageUrl,
            isManual = isManual,
            relatedQuestIds = relatedQuestIds,
            dateAdded = dateAdded
        )
    }
}
