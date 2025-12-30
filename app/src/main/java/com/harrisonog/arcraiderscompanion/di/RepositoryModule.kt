package com.harrisonog.arcraiderscompanion.di

import com.harrisonog.arcraiderscompanion.data.repository.InventoryRepositoryImpl
import com.harrisonog.arcraiderscompanion.data.repository.ItemRepositoryImpl
import com.harrisonog.arcraiderscompanion.data.repository.MapEventRepositoryImpl
import com.harrisonog.arcraiderscompanion.data.repository.QuestRepositoryImpl
import com.harrisonog.arcraiderscompanion.data.repository.WishlistRepositoryImpl
import com.harrisonog.arcraiderscompanion.domain.repository.InventoryRepository
import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
import com.harrisonog.arcraiderscompanion.domain.repository.MapEventRepository
import com.harrisonog.arcraiderscompanion.domain.repository.QuestRepository
import com.harrisonog.arcraiderscompanion.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuestRepository(
        questRepositoryImpl: QuestRepositoryImpl
    ): QuestRepository

    @Binds
    @Singleton
    abstract fun bindItemRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository

    @Binds
    @Singleton
    abstract fun bindMapEventRepository(
        mapEventRepositoryImpl: MapEventRepositoryImpl
    ): MapEventRepository
}
