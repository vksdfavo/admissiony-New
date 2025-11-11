package com.student.Compass_Abroad.modal.cityModel

data class CityModel(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    val success: Boolean = false
)