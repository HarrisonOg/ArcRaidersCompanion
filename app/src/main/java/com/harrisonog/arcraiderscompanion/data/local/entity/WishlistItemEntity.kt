package com.harrisonog.arcraiderscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItemEntity(
    @PrimaryKey
    val itemId: String,
    val itemName: String,
    val quantityNeeded: Int = 1,
    val imageUrl: String? = null,
    val isManual: Boolean = true,
    val relatedQuestIds: List<String> = emptyList(),
    val dateAdded: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)
