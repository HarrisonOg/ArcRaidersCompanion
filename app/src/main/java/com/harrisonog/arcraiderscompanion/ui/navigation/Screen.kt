package com.harrisonog.arcraiderscompanion.ui.navigation

sealed class Screen(val route: String) {
    data object QuestList : Screen("quest_list")
    data object QuestDetail : Screen("quest_detail/{questId}") {
        fun createRoute(questId: String) = "quest_detail/$questId"
    }
    data object ItemList : Screen("item_list")
    data object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: String) = "item_detail/$itemId"
    }
    data object WishlistList : Screen("wishlist_list")
}
