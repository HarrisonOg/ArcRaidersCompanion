package com.harrisonog.arcraiderscompanion.ui.item

import com.harrisonog.arcraiderscompanion.domain.model.Item

data class ItemDetailUiState(
    val item: Item? = null,
    val inventoryQuantity: Int = 0,
    val isInWishlist: Boolean = false,
    val wishlistQuantity: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ItemDetailEvent {
    data class UpdateInventoryQuantity(val quantity: Int) : ItemDetailEvent()
    data class AddToWishlist(val quantity: Int) : ItemDetailEvent()
    data object RemoveFromWishlist : ItemDetailEvent()
}
