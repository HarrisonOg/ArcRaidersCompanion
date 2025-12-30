package com.harrisonog.arcraiderscompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.harrisonog.arcraiderscompanion.ui.item.ItemDetailScreen
import com.harrisonog.arcraiderscompanion.ui.item.ItemListScreen
import com.harrisonog.arcraiderscompanion.ui.mapevent.EventDetailScreen
import com.harrisonog.arcraiderscompanion.ui.mapevent.MapEventListScreen
import com.harrisonog.arcraiderscompanion.ui.quest.QuestDetailScreen
import com.harrisonog.arcraiderscompanion.ui.quest.QuestListScreen
import com.harrisonog.arcraiderscompanion.ui.wishlist.WishlistListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.QuestList.route,
    onOpenDrawer: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.QuestList.route) {
            QuestListScreen(
                onNavigateToQuestDetail = { questId ->
                    navController.navigate(Screen.QuestDetail.createRoute(questId))
                },
                onNavigateToItemList = {
                    navController.navigate(Screen.ItemList.route)
                },
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(
            route = Screen.QuestDetail.route,
            arguments = listOf(
                navArgument("questId") { type = NavType.StringType }
            )
        ) {
            QuestDetailScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToItem = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                }
            )
        }

        composable(Screen.ItemList.route) {
            ItemListScreen(
                onNavigateToItemDetail = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType }
            )
        ) {
            ItemDetailScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.WishlistList.route) {
            WishlistListScreen(
                onNavigateToItemDetail = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.MapEventList.route) {
            MapEventListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onOpenDrawer = onOpenDrawer,
                onNavigateToEventDetail = { eventId ->
                    navController.navigate(Screen.MapEventDetail.createRoute(eventId))
                }
            )
        }

        composable(
            route = Screen.MapEventDetail.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
            )
        ) {
            EventDetailScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
