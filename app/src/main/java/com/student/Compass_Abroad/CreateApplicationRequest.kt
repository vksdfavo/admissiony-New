package com.student.Compass_Abroad

data class CreateApplicationRequest(
    val intake: Int,
    val intake_year: Int,
    val institution: Int,
    val destination_country: Int,
    val campus: Int,
    val lead_identifier: String,
    val programs: List<Int>
)