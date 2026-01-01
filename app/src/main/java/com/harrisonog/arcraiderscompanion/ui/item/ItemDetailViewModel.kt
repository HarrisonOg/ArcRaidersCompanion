package com.harrisonog.arcraiderscompanion.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.repository.InventoryRepository
import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
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
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val inventoryRepository: InventoryRepository,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow(ItemDetailUiState())
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    fun onEvent(event: ItemDetailEvent) {
        when (event) {
            is ItemDetailEvent.UpdateInventoryQuantity -> updateInventoryQuantity(event.quantity)
            is ItemDetailEvent.AddToWishlist -> addToWishlist(event.quantity)
            is ItemDetailEvent.RemoveFromWishlist -> removeFromWishlist()
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Defensive: ensure items are synced before loading
            // This handles edge case where user navigates before app initialization completes
            // Smart sync: no-op if items already synced (< 21 days)
            itemRepository.syncItems()

            combine(
                itemRepository.getItemById(itemId),
                inventoryRepository.getInventoryItem(itemId),
                wishlistRepository.getWishlistItem(itemId)
            ) { item, inventory, wishlist ->
                Triple(item, inventory, wishlist)
            }.collect { (item, inventory, wishlist) ->
                _uiState.update {
                    it.copy(
                        item = item,
                        inventoryQuantity = inventory?.quantity ?: 0,
                        isInWishlist = wishlist != null,
                        wishlistQuantity = wishlist?.quantityNeeded ?: 0,
                        isLoading = false,
                        error = if (item == null) "Item not found" else null
                    )
                }
            }
        }
    }

    private fun updateInventoryQuantity(quantity: Int) {
        val currentItem = _uiState.value.item ?: return
        viewModelScope.launch {
            inventoryRepository.updateItemQuantity(
                currentItem.id,
                currentItem.name,
                quantity,
                currentItem.imageUrl
            )
        }
    }

    private fun addToWishlist(quantity: Int) {
        val currentItem = _uiState.value.item ?: return
        viewModelScope.launch {
            wishlistRepository.addToWishlist(
                itemId = currentItem.id,
                itemName = currentItem.name,
                quantity = quantity,
                imageUrl = currentItem.imageUrl,
                isManual = true,
                questId = null
            )
        }
    }

    private fun removeFromWishlist() {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(itemId)
        }
    }
}
