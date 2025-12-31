package com.harrisonog.arcraiderscompanion.data.local.model

import com.google.gson.annotations.SerializedName

/**
 * Root object for event_descriptions.json
 */
data class EventDescriptionsData(
    @SerializedName("descriptions")
    val descriptions: List<EventDescription>
)

/**
 * Individual event description entry
 */
data class EventDescription(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String
)
