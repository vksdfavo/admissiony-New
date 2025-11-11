package com.student.Compass_Abroad.modal.getDestinationCountryList

data class getDestinationCountry(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)