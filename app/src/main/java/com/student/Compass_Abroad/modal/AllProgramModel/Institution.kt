package com.student.Compass_Abroad.modal.AllProgramModel

data class Institution(
    val commission_currency: String,
    val country: Country,
    val identifier: String,
    val logo: String,
    val name: String,
    val url: String,
    val country_id: Int,
    val has_accommodation: String,
    val accommodation_options: String
)