package com.student.Compass_Abroad.Utils

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatToLocalTime(utcDate: String): String {
    val zonedDateTime = ZonedDateTime.parse(utcDate)
    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()) // Convert to local timezone
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a") // Example: 19 Dec 2024, 09:56 AM
    return localDateTime.format(formatter)
}