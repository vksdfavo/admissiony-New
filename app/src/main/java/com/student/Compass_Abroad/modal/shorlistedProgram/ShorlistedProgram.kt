package com.student.Compass_Abroad.modal.shorlistedProgram

data class ShorlistedProgram(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)