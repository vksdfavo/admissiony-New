package com.student.Compass_Abroad.modal.formCountryModel

data class formCountryResponse(
    val `data`: List<Data>,
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val success: Boolean
)