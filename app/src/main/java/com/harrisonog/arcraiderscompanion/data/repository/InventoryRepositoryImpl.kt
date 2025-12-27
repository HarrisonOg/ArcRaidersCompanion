package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.InventoryDao
import com.harrisonog.arcraiderscompanion.data.local.entity.InventoryItemEntity
import com.harrisonog.arcraiderscompanion.domain.model.InventoryItem
import com.harrisonog.arcraiderscompanion.domain.repository.InventoryRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val inventoryDao: InventoryDao
) : InventoryRepository {

    override fun getAllInventoryItems(): Flow<List<InventoryItem>> {
        return inventoryDao.getAllInventoryItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getInventoryItem(itemId: String): Flow<InventoryItem?> {
        return inventoryDao.getInventoryItem(itemId).map { it?.toDomain() }
    }

    override fun getInventoryItemsByIds(itemIds: List<String>): Flow<List<InventoryItem>> {
        return inventoryDao.getInventoryItemsByIds(itemIds).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateItemQuantity(
        itemId: String,
        itemName: String,
        quantity: Int,
        imageUrl: String?
    ): Result<Unit> {
        return try {
            val existing = inventoryDao.getInventoryItemSync(itemId)
            if (existing != null) {
                inventoryDao.updateQuantity(itemId, quantity.coerceAtLeast(0))
            } else {
                inventoryDao.insertInventoryItem(
                    InventoryItemEntity(
                        itemId = itemId,
                        itemName = itemName,
                        quantity = quantity.coerceAtLeast(0),
                        imageUrl = imageUrl
                    )
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun incrementItemQuantity(
        itemId: String,
        itemName: String,
        imageUrl: String?
    ): Result<Unit> {
        return try {
            val existing = inventoryDao.getInventoryItemSync(itemId)
            if (existing != null) {
                inventoryDao.updateQuantity(itemId, existing.quantity + 1)
            } else {
                inventoryDao.insertInventoryItem(
                    InventoryItemEntity(
                        itemId = itemId,
                        itemName = itemName,
                        quantity = 1,
                        imageUrl = imageUrl
                    )
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun decrementItemQuantity(itemId: String): Result<Unit> {
        return try {
            val existing = inventoryDao.getInventoryItemSync(itemId)
            if (existing != null && existing.quantity > 0) {
                inventoryDao.updateQuantity(itemId, existing.quantity - 1)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCollectedItemCount(): Int {
        return inventoryDao.getCollectedItemCount()
    }

    private fun InventoryItemEntity.toDomain(): InventoryItem {
        return InventoryItem(
            itemId = itemId,
            itemName = itemName,
            quantity = quantity,
            imageUrl = imageUrl
        )
    }
}
