package com.student.Compass_Abroad

data class SavePreferencesRequest(
    val disciplines: List<String>,
    val destination_country: String,
    val preferred_study_level: String

)
