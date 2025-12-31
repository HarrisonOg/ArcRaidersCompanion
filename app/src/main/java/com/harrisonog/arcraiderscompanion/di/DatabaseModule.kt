package com.harrisonog.arcraiderscompanion.di

import android.content.Context
import androidx.room.Room
import com.harrisonog.arcraiderscompanion.data.local.AppDatabase
import com.harrisonog.arcraiderscompanion.data.local.dao.InventoryDao
import com.harrisonog.arcraiderscompanion.data.local.dao.ItemDao
import com.harrisonog.arcraiderscompanion.data.local.dao.MapEventDao
import com.harrisonog.arcraiderscompanion.data.local.dao.QuestDao
import com.harrisonog.arcraiderscompanion.data.local.dao.WishlistDao
import com.harrisonog.arcraiderscompanion.data.local.dao.WorkshopUpgradeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideQuestDao(database: AppDatabase): QuestDao {
        return database.questDao()
    }

    @Provides
    @Singleton
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    @Singleton
    fun provideInventoryDao(database: AppDatabase): InventoryDao {
        return database.inventoryDao()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: AppDatabase): WishlistDao {
        return database.wishlistDao()
    }

    @Provides
    @Singleton
    fun provideMapEventDao(database: AppDatabase): MapEventDao {
        return database.mapEventDao()
    }

    @Provides
    @Singleton
    fun provideWorkshopUpgradeDao(database: AppDatabase): WorkshopUpgradeDao {
        return database.workshopUpgradeDao()
    }
}
