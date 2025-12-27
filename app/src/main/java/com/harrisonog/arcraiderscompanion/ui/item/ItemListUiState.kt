package com.harrisonog.arcraiderscompanion.ui.item

import com.harrisonog.arcraiderscompanion.domain.model.Item
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity

data class ItemListUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterCategory: ItemCategory? = null,
    val filterRarity: ItemRarity? = null,
    val searchQuery: String = "",
    val showQuestItemsOnly: Boolean = false,
    val isRefreshing: Boolean = false
)

sealed class ItemListEvent {
    data object RefreshItems : ItemListEvent()
    data class FilterByCategory(val category: ItemCategory?) : ItemListEvent()
    data class FilterByRarity(val rarity: ItemRarity?) : ItemListEvent()
    data class Search(val query: String) : ItemListEvent()
    data class ToggleQuestItemsOnly(val enabled: Boolean) : ItemListEvent()
    data class NavigateToItemDetail(val itemId: String) : ItemListEvent()
}
