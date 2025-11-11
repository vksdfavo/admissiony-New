package com.student.Compass_Abroad.Utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.util.Locale

fun formatUtcToLocalTime(utcDate: String?): String {
    if (utcDate.isNullOrEmpty()) return ""

    return try {
        val zonedUTC = ZonedDateTime.parse(utcDate) // Parse UTC
        val indiaTime = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata")) // Convert to IST

        val day = indiaTime.dayOfMonth
        val daySuffix = getDayOfMonthSuffix(day)

        val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

        "$day$daySuffix ${indiaTime.format(dateFormatter)}, ${indiaTime.format(formatter)}"
    } catch (e: Exception) {
        ""
    }
}

// For "1st", "2nd", "3rd", "4th" etc.
fun getDayOfMonthSuffix(n: Int): String {
    if (n in 11..13) return "th"
    return when (n % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}
