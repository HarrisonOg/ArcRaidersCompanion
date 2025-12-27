package com.harrisonog.arcraiderscompanion.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrisonog.arcraiderscompanion.domain.model.ItemCategory
import com.harrisonog.arcraiderscompanion.domain.model.ItemRarity
import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemListUiState())
    val uiState: StateFlow<ItemListUiState> = _uiState.asStateFlow()

    init {
        loadItems()
        syncItems()
    }

    fun onEvent(event: ItemListEvent) {
        when (event) {
            is ItemListEvent.RefreshItems -> refreshItems()
            is ItemListEvent.FilterByCategory -> filterByCategory(event.category)
            is ItemListEvent.FilterByRarity -> filterByRarity(event.rarity)
            is ItemListEvent.Search -> search(event.query)
            is ItemListEvent.ToggleQuestItemsOnly -> toggleQuestItemsOnly(event.enabled)
            is ItemListEvent.NavigateToItemDetail -> {
                // Navigation handled by UI
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val flow = when {
                _uiState.value.searchQuery.isNotBlank() -> {
                    itemRepository.searchItems(_uiState.value.searchQuery)
                }
                _uiState.value.showQuestItemsOnly -> {
                    itemRepository.getQuestItems()
                }
                _uiState.value.filterCategory != null -> {
                    itemRepository.getItemsByCategory(_uiState.value.filterCategory!!)
                }
                _uiState.value.filterRarity != null -> {
                    itemRepository.getItemsByRarity(_uiState.value.filterRarity!!)
                }
                else -> {
                    itemRepository.getAllItems()
                }
            }

            flow.collect { items ->
                _uiState.update {
                    it.copy(
                        items = items,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun syncItems() {
        viewModelScope.launch {
            when (val result = itemRepository.syncItems()) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to sync items"
                        )
                    }
                }
                is Result.Success -> {
                    // Successfully synced
                }
                Result.Loading -> {}
            }
        }
    }

    private fun refreshItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            when (val result = itemRepository.refreshItems()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message ?: "Failed to refresh items"
                        )
                    }
                }
                Result.Loading -> {}
            }
        }
    }

    private fun filterByCategory(category: ItemCategory?) {
        _uiState.update {
            it.copy(
                filterCategory = category,
                filterRarity = null,
                showQuestItemsOnly = false,
                searchQuery = ""
            )
        }
        loadItems()
    }

    private fun filterByRarity(rarity: ItemRarity?) {
        _uiState.update {
            it.copy(
                filterRarity = rarity,
                filterCategory = null,
                showQuestItemsOnly = false,
                searchQuery = ""
            )
        }
        loadItems()
    }

    private fun search(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filterCategory = null,
                filterRarity = null,
                showQuestItemsOnly = false
            )
        }
        loadItems()
    }

    private fun toggleQuestItemsOnly(enabled: Boolean) {
        _uiState.update {
            it.copy(
                showQuestItemsOnly = enabled,
                filterCategory = null,
                filterRarity = null,
                searchQuery = ""
            )
        }
        loadItems()
    }
}
