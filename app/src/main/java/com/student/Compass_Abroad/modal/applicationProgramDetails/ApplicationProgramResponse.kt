package com.student.Compass_Abroad.modal.applicationProgramDetails

data class ApplicationProgramResponse(
    val `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)