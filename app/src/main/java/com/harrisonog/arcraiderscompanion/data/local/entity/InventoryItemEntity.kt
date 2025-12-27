package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryItemEntity(
    @PrimaryKey
    val itemId: String,
    val itemName: String,
    val quantity: Int = 0,
    val imageUrl: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
