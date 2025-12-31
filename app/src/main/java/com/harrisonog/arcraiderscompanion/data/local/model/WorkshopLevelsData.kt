package com.harrisonog.arcraiderscompanion.data.local.model

import com.google.gson.annotations.SerializedName
import com.harrisonog.arcraiderscompanion.domain.model.WorkshopUpgrade

/**
 * Root object for workshop_levels.json
 */
data class WorkshopLevelsData(
    @SerializedName("workshopStations")
    val workshopStations: List<WorkshopStationData>
)

/**
 * Workshop station with levels
 */
data class WorkshopStationData(
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("stationName")
    val stationName: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("levels")
    val levels: List<WorkshopUpgrade>
)
