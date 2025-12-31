package com.harrisonog.arcraiderscompanion.ui.workshop

import com.harrisonog.arcraiderscompanion.domain.model.RequiredItemWithInventory
import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopUpgrade

data class WorkshopDetailUiState(
    val upgrade: WorkshopUpgrade? = null,
    val requiredItemsWithInventory: List<RequiredItemWithInventory> = emptyList(),
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null
)

sealed class WorkshopDetailEvent {
    data object StartUpgrade : WorkshopDetailEvent()
    data object CompleteUpgrade : WorkshopDetailEvent()
    data class UpdateStatus(val status: UpgradeStatus) : WorkshopDetailEvent()
    data class UpdateItemQuantity(
        val itemId: String,
        val itemName: String,
        val quantity: Int,
        val imageUrl: String?
    ) : WorkshopDetailEvent()
}
