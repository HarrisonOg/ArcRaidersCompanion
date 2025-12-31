package com.harrisonog.arcraiderscompanion.ui.workshop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.model.RequiredItemWithInventory
import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import com.harrisonog.arcraiderscompanion.domain.repository.InventoryRepository
import com.harrisonog.arcraiderscompanion.domain.repository.WishlistRepository
import com.harrisonog.arcraiderscompanion.domain.repository.WorkshopUpgradeRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkshopDetailViewModel @Inject constructor(
    private val workshopUpgradeRepository: WorkshopUpgradeRepository,
    private val inventoryRepository: InventoryRepository,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val levelId: String = checkNotNull(savedStateHandle["levelId"])

    private val _uiState = MutableStateFlow(WorkshopDetailUiState())
    val uiState: StateFlow<WorkshopDetailUiState> = _uiState.asStateFlow()

    init {
        loadUpgrade()
    }

    fun onEvent(event: WorkshopDetailEvent) {
        when (event) {
            is WorkshopDetailEvent.StartUpgrade -> startUpgrade()
            is WorkshopDetailEvent.CompleteUpgrade -> completeUpgrade()
            is WorkshopDetailEvent.UpdateStatus -> updateStatus(event.status)
            is WorkshopDetailEvent.UpdateItemQuantity -> updateItemQuantity(
                event.itemId,
                event.itemName,
                event.quantity,
                event.imageUrl
            )
        }
    }

    private fun loadUpgrade() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                workshopUpgradeRepository.getUpgradeById(levelId),
                inventoryRepository.getAllInventoryItems()
            ) { upgrade, inventoryItems ->
                val inventoryMap = inventoryItems.associateBy { it.itemId }

                val requiredItemsWithInventory = upgrade?.requiredItems?.map { required ->
                    val inventoryItem = inventoryMap[required.itemId]
                    RequiredItemWithInventory(
                        itemId = required.itemId,
                        itemName = required.itemName,
                        quantityNeeded = required.quantity,
                        quantityOwned = inventoryItem?.quantity ?: 0,
                        imageUrl = required.imageUrl
                    )
                } ?: emptyList()

                Pair(upgrade, requiredItemsWithInventory)
            }.collect { (upgrade, requiredItemsWithInventory) ->
                _uiState.update {
                    it.copy(
                        upgrade = upgrade,
                        requiredItemsWithInventory = requiredItemsWithInventory,
                        isLoading = false,
                        error = if (upgrade == null) "Upgrade not found" else null
                    )
                }
            }
        }
    }

    private fun updateItemQuantity(
        itemId: String,
        itemName: String,
        quantity: Int,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            inventoryRepository.updateItemQuantity(itemId, itemName, quantity, imageUrl)
        }
    }

    private fun startUpgrade() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }

            when (val result = workshopUpgradeRepository.startUpgrade(levelId)) {
                is Result.Success -> {
                    // Add items to wishlist when starting
                    val upgrade = _uiState.value.upgrade
                    if (upgrade != null && upgrade.requiredItems.isNotEmpty()) {
                        wishlistRepository.addWorkshopItemsToWishlist(levelId, upgrade.requiredItems)
                    }

                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = result.message ?: "Failed to start upgrade"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }

    private fun completeUpgrade() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }

            when (val result = workshopUpgradeRepository.completeUpgrade(levelId)) {
                is Result.Success -> {
                    // Remove items from wishlist when completing
                    wishlistRepository.removeWorkshopItemsFromWishlist(levelId)

                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = result.message ?: "Failed to complete upgrade"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }

    private fun updateStatus(status: UpgradeStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }

            when (val result = workshopUpgradeRepository.updateUpgradeStatus(levelId, status)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = result.message ?: "Failed to update status"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }
}
