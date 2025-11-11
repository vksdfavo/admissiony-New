package com.student.Compass_Abroad.modal.destinationCountry

data class ApplicationDestinationCountry(
    val `data`: List<Data>,
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val success: Boolean
)