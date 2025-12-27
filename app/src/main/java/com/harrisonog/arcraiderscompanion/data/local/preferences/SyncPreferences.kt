package com.harrisonog.arcraiderscompanion.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sync_preferences")

@Singleton
class SyncPreferences @Inject constructor(
    private val context: Context
) {
    private val lastQuestSyncKey = longPreferencesKey("last_quest_sync")
    private val lastItemSyncKey = longPreferencesKey("last_item_sync")

    companion object {
        private const val THREE_WEEKS_IN_MILLIS = 21L * 24 * 60 * 60 * 1000 // 21 days
    }

    suspend fun getLastQuestSync(): Long {
        return context.dataStore.data.first()[lastQuestSyncKey] ?: 0L
    }

    suspend fun setLastQuestSync(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[lastQuestSyncKey] = timestamp
        }
    }

    suspend fun getLastItemSync(): Long {
        return context.dataStore.data.first()[lastItemSyncKey] ?: 0L
    }

    suspend fun setLastItemSync(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[lastItemSyncKey] = timestamp
        }
    }

    suspend fun shouldSyncQuests(): Boolean {
        val lastSync = getLastQuestSync()
        if (lastSync == 0L) return true // Never synced before
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastSync) >= THREE_WEEKS_IN_MILLIS
    }

    suspend fun shouldSyncItems(): Boolean {
        val lastSync = getLastItemSync()
        if (lastSync == 0L) return true // Never synced before
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastSync) >= THREE_WEEKS_IN_MILLIS
    }
}
