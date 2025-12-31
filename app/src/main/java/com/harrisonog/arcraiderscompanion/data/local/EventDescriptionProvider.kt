package com.harrisonog.arcraiderscompanion.data.local

import android.content.Context
import com.google.gson.Gson
import com.harrisonog.arcraiderscompanion.data.local.model.EventDescriptionsData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton provider for event descriptions loaded from assets/event_descriptions.json
 *
 * Descriptions are lazy-loaded on first access and cached in memory for the app lifecycle.
 * Provides O(1) lookup by event name.
 */
@Singleton
class EventDescriptionProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    /**
     * Lazy-loaded map of event name -> description
     * Loaded from assets/event_descriptions.json on first access
     */
    private val descriptionsMap: Map<String, String> by lazy {
        loadDescriptions()
    }

    /**
     * Get description for a given event name
     *
     * @param eventName The name of the event to get description for
     * @return The event description, or null if not found
     */
    fun getDescription(eventName: String): String? {
        return descriptionsMap[eventName]
    }

    /**
     * Load and parse event descriptions from assets
     *
     * @return Map of event name to description, or empty map on error
     */
    private fun loadDescriptions(): Map<String, String> {
        return try {
            context.assets.open("event_descriptions.json").use { inputStream ->
                val json = inputStream.bufferedReader().use { it.readText() }
                val data = gson.fromJson(json, EventDescriptionsData::class.java)

                // Convert list to map for O(1) lookup
                data.descriptions.associate { it.name to it.description }
            }
        } catch (e: Exception) {
            // Return empty map on error - descriptions are optional enhancements
            emptyMap()
        }
    }
}
