package com.harrisonog.arcraiderscompanion.di

import com.harrisonog.arcraiderscompanion.data.repository.ItemRepositoryImpl
import com.harrisonog.arcraiderscompanion.data.repository.QuestRepositoryImpl
import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
import com.harrisonog.arcraiderscompanion.domain.repository.QuestRepository
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
}
