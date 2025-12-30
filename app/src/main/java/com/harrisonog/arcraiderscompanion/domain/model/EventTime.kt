package com.harrisonog.arcraiderscompanion.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class EventTime(
    val startTime: String, // Format: "HH:mm" in UTC
    val endTime: String    // Format: "HH:mm" in UTC
) : Parcelable {

    /**
     * Converts UTC times to local device timezone
     * @return Pair of (localStartTime, localEndTime) in "HH:mm" format
     */
    fun toLocalTime(): Pair<String, String> {
        return try {
            val utcZone = ZoneId.of("UTC")
            val localZone = ZoneId.systemDefault()
            val today = ZonedDateTime.now(utcZone).toLocalDate()

            // Parse start time
            val startLocalTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
            val startUtc = ZonedDateTime.of(today, startLocalTime, utcZone)
            val startLocal = startUtc.withZoneSameInstant(localZone)

            // Parse end time
            val endLocalTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
            val endUtc = ZonedDateTime.of(today, endLocalTime, utcZone)
            val endLocal = endUtc.withZoneSameInstant(localZone)

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            Pair(
                startLocal.format(formatter),
                endLocal.format(formatter)
            )
        } catch (e: Exception) {
            // Fallback to original times if parsing fails
            Pair(startTime, endTime)
        }
    }

    /**
     * Formats the time range for display in local timezone
     * @return Formatted string like "14:00 - 15:00"
     */
    fun formatForDisplay(): String {
        val (localStart, localEnd) = toLocalTime()
        return "$localStart - $localEnd"
    }
}
