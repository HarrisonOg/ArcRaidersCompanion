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

    /**
     * Checks if this event time is upcoming (starting within the next 2 hours)
     * @return true if the event starts within the next 2 hours
     */
    fun isUpcoming(): Boolean {
        return try {
            val now = ZonedDateTime.now()
            val nextOccurrence = getNextOccurrence()
            nextOccurrence?.let {
                val hoursUntil = java.time.Duration.between(now, it).toHours()
                hoursUntil in 0..2
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Gets the next occurrence of this event time
     * @return ZonedDateTime of the next occurrence, or null if parsing fails
     */
    fun getNextOccurrence(): ZonedDateTime? {
        return try {
            val utcZone = ZoneId.of("UTC")
            val localZone = ZoneId.systemDefault()
            val now = ZonedDateTime.now()

            // Parse start time in UTC
            val startLocalTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))

            // Try today first
            val today = now.toLocalDate()
            val todayUtc = ZonedDateTime.of(today, startLocalTime, utcZone)
            val todayLocal = todayUtc.withZoneSameInstant(localZone)

            if (todayLocal.isAfter(now)) {
                return todayLocal
            }

            // If not today, try tomorrow
            val tomorrow = today.plusDays(1)
            val tomorrowUtc = ZonedDateTime.of(tomorrow, startLocalTime, utcZone)
            tomorrowUtc.withZoneSameInstant(localZone)
        } catch (e: Exception) {
            null
        }
    }
}
