package com.student.Compass_Abroad

data class DateItem(
    val label: String,
    val date: String,
    val apiDate: String,  // API (2025-10-15)
    var isSelected: Boolean = false,
    val isSunday: Boolean = false
)


