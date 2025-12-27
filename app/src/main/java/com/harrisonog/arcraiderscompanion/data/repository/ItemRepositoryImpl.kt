package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.ItemDao
import com.harrisonog.arcraiderscompanion.data.local.entity.toDomain
import com.harrisonog.arcraiderscompanion.data.local.entity.toEntity
import com.harrisonog.arcraiderscompanion.data.mapper.toDomain
import com.harrisonog.arcraiderscompanion.data.remote.MetaForgeApi
import com.harrisonog.arcraiderscompanion.domain.model.Item
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity
import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val api: MetaForgeApi,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getAllItems(): Flow<List<Item>> {
        return itemDao.getAllItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getItemById(itemId: String): Flow<Item?> {
        return itemDao.getItemById(itemId).map { it?.toDomain() }
    }

    override fun getItemsByCategory(category: ItemCategory): Flow<List<Item>> {
        return itemDao.getItemsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getItemsByRarity(rarity: ItemRarity): Flow<List<Item>> {
        return itemDao.getItemsByRarity(rarity).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestItems(): Flow<List<Item>> {
        return itemDao.getQuestItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getItemsByIds(itemIds: List<String>): Flow<List<Item>> {
        return itemDao.getItemsByIds(itemIds).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchItems(query: String): Flow<List<Item>> {
        return itemDao.searchItems(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshItems(): Result<Unit> {
        return try {
            val itemDtos = api.getItems()
            val items = itemDtos.mapNotNull { it.toDomain() }
            itemDao.insertItems(items.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncItems(): Result<Unit> {
        val itemCount = itemDao.getItemCount()
        return if (itemCount == 0) {
            refreshItems()
        } else {
            Result.Success(Unit)
        }
    }
}
