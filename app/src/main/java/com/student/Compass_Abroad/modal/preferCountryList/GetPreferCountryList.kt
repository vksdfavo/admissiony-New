package com.student.Compass_Abroad.modal.preferCountryList

data class GetPreferCountryList(
    val `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)