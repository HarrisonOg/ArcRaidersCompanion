package com.harrisonog.arcraiderscompanion.ui.item

import com.harrisonog.arcraiderscompanion.domain.model.Item

data class ItemDetailUiState(
    val item: Item? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
