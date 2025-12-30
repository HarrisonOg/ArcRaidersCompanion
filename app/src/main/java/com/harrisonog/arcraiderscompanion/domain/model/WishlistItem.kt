package com.harrisonog.arcraiderscompanion.domain.model

data class WishlistItem(
    val itemId: String,
    val itemName: String,
    val quantityNeeded: Int,
    val imageUrl: String? = null,
    val isManual: Boolean,
    val relatedQuestIds: List<String>,
    val dateAdded: Long
)

data class WishlistItemWithInventory(
    val itemId: String,
    val itemName: String,
    val quantityNeeded: Int,
    val quantityOwned: Int = 0,
    val imageUrl: String? = null,
    val isManual: Boolean,
    val relatedQuestIds: List<String>,
    val dateAdded: Long
) {
    val isComplete: Boolean get() = quantityOwned >= quantityNeeded
    val percentComplete: Float get() = if (quantityNeeded > 0) {
        (quantityOwned.toFloat() / quantityNeeded).coerceIn(0f, 1f)
    } else 1f
    val remainingNeeded: Int get() = (quantityNeeded - quantityOwned).coerceAtLeast(0)
}
