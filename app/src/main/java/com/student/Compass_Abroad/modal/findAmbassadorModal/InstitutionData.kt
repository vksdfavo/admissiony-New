package com.student.Compass_Abroad.modal.findAmbassadorModal

data class InstitutionData(
    val campus_data: CampusData,
    val country_data: CountryData,
    val id: Int,
    val identifier: String,
    val name: String
)