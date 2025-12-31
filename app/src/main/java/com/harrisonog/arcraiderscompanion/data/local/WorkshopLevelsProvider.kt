package com.harrisonog.arcraiderscompanion.data.local

import android.content.Context
import com.google.gson.Gson
import com.harrisonog.arcraiderscompanion.data.local.model.WorkshopLevelsData
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopUpgrade
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton provider for workshop levels loaded from assets/workshop_levels.json
 *
 * Data is lazy-loaded on first access and cached in memory for the app lifecycle.
 */
@Singleton
class WorkshopLevelsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    /**
     * Lazy-loaded list of all workshop upgrades from JSON
     */
    private val upgrades: List<WorkshopUpgrade> by lazy {
        loadUpgrades()
    }

    /**
     * Lazy-loaded map of stationId -> station metadata
     */
    private val stationMetadata: Map<String, StationMetadata> by lazy {
        loadStationMetadata()
    }

    /**
     * Get all workshop upgrades
     */
    fun getAllUpgrades(): List<WorkshopUpgrade> = upgrades

    /**
     * Get station metadata by ID
     */
    fun getStationMetadata(stationId: String): StationMetadata? = stationMetadata[stationId]

    /**
     * Get all station metadata
     */
    fun getAllStationMetadata(): List<StationMetadata> = stationMetadata.values.toList()

    /**
     * Load upgrades from JSON
     */
    private fun loadUpgrades(): List<WorkshopUpgrade> {
        return try {
            context.assets.open("workshop_levels.json").use { inputStream ->
                val json = inputStream.bufferedReader().use { it.readText() }
                val data = gson.fromJson(json, WorkshopLevelsData::class.java)

                data.workshopStations.flatMap { station ->
                    station.levels
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Load station metadata from JSON
     */
    private fun loadStationMetadata(): Map<String, StationMetadata> {
        return try {
            context.assets.open("workshop_levels.json").use { inputStream ->
                val json = inputStream.bufferedReader().use { it.readText() }
                val data = gson.fromJson(json, WorkshopLevelsData::class.java)

                data.workshopStations.associate { station ->
                    station.stationId to StationMetadata(
                        stationId = station.stationId,
                        stationName = station.stationName,
                        description = station.description,
                        imageUrl = station.imageUrl
                    )
                }
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    data class StationMetadata(
        val stationId: String,
        val stationName: String,
        val description: String?,
        val imageUrl: String?
    )
}
