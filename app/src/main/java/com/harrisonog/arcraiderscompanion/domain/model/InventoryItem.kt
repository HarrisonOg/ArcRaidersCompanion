package com.harrisonog.arcraiderscompanion.domain.model

data class InventoryItem(
    val itemId: String,
    val itemName: String,
    val quantity: Int = 0,
    val imageUrl: String? = null
)

data class RequiredItemWithInventory(
    val itemId: String,
    val itemName: String,
    val quantityNeeded: Int,
    val quantityOwned: Int = 0,
    val imageUrl: String? = null
) {
    val isComplete: Boolean get() = quantityOwned >= quantityNeeded
    val percentComplete: Float get() = if (quantityNeeded > 0) {
        (quantityOwned.toFloat() / quantityNeeded).coerceIn(0f, 1f)
    } else 1f
}
