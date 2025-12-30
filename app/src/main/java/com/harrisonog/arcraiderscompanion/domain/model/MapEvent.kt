package com.harrisonog.arcraiderscompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapEvent(
    val id: String,
    val name: String,
    val map: String,
    val iconUrl: String? = null,
    val description: String? = null,
    val times: List<EventTime> = emptyList()
) : Parcelable
