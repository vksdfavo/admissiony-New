package com.student.Compass_Abroad.modal.UtmModal

data class UtmModalResponse(
    val `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)