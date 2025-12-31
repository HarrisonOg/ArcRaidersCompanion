package com.harrisonog.arcraiderscompanion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harrisonog.arcraiderscompanion.data.local.converters.Converters
import com.harrisonog.arcraiderscompanion.data.local.dao.InventoryDao
import com.harrisonog.arcraiderscompanion.data.local.dao.ItemDao
import com.harrisonog.arcraiderscompanion.data.local.dao.MapEventDao
import com.harrisonog.arcraiderscompanion.data.local.dao.QuestDao
import com.harrisonog.arcraiderscompanion.data.local.dao.WishlistDao
import com.harrisonog.arcraiderscompanion.data.local.dao.WorkshopUpgradeDao
import com.harrisonog.arcraiderscompanion.data.local.entity.InventoryItemEntity
import com.harrisonog.arcraiderscompanion.data.local.entity.ItemEntity
import com.harrisonog.arcraiderscompanion.data.local.entity.MapEventEntity
import com.harrisonog.arcraiderscompanion.data.local.entity.QuestEntity
import com.harrisonog.arcraiderscompanion.data.local.entity.WishlistItemEntity
import com.harrisonog.arcraiderscompanion.data.local.entity.WorkshopUpgradeEntity

@Database(
    entities = [
        QuestEntity::class,
        ItemEntity::class,
        InventoryItemEntity::class,
        WishlistItemEntity::class,
        MapEventEntity::class,
        WorkshopUpgradeEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao
    abstract fun itemDao(): ItemDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun mapEventDao(): MapEventDao
    abstract fun workshopUpgradeDao(): WorkshopUpgradeDao

    companion object {
        const val DATABASE_NAME = "arc_raiders_companion_db"
    }
}
