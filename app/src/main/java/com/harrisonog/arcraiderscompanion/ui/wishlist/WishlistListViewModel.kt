package com.harrisonog.arcraiderscompanion.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.model.WishlistItemWithInventory
import com.harrisonog.arcraiderscompanion.domain.repository.InventoryRepository
import com.harrisonog.arcraiderscompanion.domain.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistListViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishlistListUiState())
    val uiState: StateFlow<WishlistListUiState> = _uiState.asStateFlow()

    init {
        loadWishlistItems()
    }

    fun onEvent(event: WishlistListEvent) {
        when (event) {
            is WishlistListEvent.RemoveItem -> removeItem(event.itemId)
            is WishlistListEvent.UpdateQuantity -> updateQuantity(event.itemId, event.itemName, event.quantity, event.imageUrl)
            is WishlistListEvent.ToggleShowCompleted -> toggleShowCompleted(event.show)
            is WishlistListEvent.NavigateToItemDetail -> {} // Handled by screen
        }
    }

    private fun loadWishlistItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                wishlistRepository.getAllWishlistItems(),
                inventoryRepository.getAllInventoryItems()
            ) { wishlistItems, inventoryItems ->
                val inventoryMap = inventoryItems.associateBy { it.itemId }

                wishlistItems.map { wishlist ->
                    val inventory = inventoryMap[wishlist.itemId]
                    WishlistItemWithInventory(
                        itemId = wishlist.itemId,
                        itemName = wishlist.itemName,
                        quantityNeeded = wishlist.quantityNeeded,
                        quantityOwned = inventory?.quantity ?: 0,
                        imageUrl = wishlist.imageUrl,
                        isManual = wishlist.isManual,
                        relatedQuestIds = wishlist.relatedQuestIds,
                        dateAdded = wishlist.dateAdded
                    )
                }
            }.collect { items ->
                _uiState.update {
                    it.copy(
                        items = items,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun removeItem(itemId: String) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(itemId)
        }
    }

    private fun updateQuantity(itemId: String, itemName: String, quantity: Int, imageUrl: String?) {
        viewModelScope.launch {
            inventoryRepository.updateItemQuantity(itemId, itemName, quantity, imageUrl)
        }
    }

    private fun toggleShowCompleted(show: Boolean) {
        _uiState.update { it.copy(showCompleted = show) }
    }
}
