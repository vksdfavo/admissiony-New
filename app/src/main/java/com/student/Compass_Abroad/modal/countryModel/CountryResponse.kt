package com.student.Compass_Abroad.modal.countryModel

data class CountryResponse(
    var `data`: List<DataX>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)