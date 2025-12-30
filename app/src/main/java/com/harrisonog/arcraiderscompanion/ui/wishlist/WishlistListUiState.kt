package com.harrisonog.arcraiderscompanion.ui.wishlist

import com.harrisonog.arcraiderscompanion.domain.model.WishlistItemWithInventory

data class WishlistListUiState(
    val items: List<WishlistItemWithInventory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showCompleted: Boolean = true
)

sealed class WishlistListEvent {
    data class RemoveItem(val itemId: String) : WishlistListEvent()
    data class UpdateQuantity(val itemId: String, val itemName: String, val quantity: Int, val imageUrl: String?) : WishlistListEvent()
    data class ToggleShowCompleted(val show: Boolean) : WishlistListEvent()
    data class NavigateToItemDetail(val itemId: String) : WishlistListEvent()
}
