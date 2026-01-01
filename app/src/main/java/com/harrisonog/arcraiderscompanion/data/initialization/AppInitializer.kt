package com.harrisonog.arcraiderscompanion.data.initialization

import com.harrisonog.arcraiderscompanion.domain.repository.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles application-level initialization tasks.
 *
 * Responsible for:
 * - Syncing items from MetaForge API on app startup
 * - Future: syncing quests, map events, etc.
 */
@Singleton
class AppInitializer @Inject constructor(
    private val itemRepository: ItemRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Initialize app-level data syncing.
     *
     * This runs in the background and does not block app startup.
     * Smart sync logic ensures data is only fetched when needed:
     * - First app launch (empty database)
     * - > 21 days since last sync
     */
    fun initialize() {
        scope.launch {
            // Smart sync: only if empty or > 21 days
            itemRepository.syncItems()
        }
    }
}
