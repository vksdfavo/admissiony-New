package com.student.Compass_Abroad.modal.forgotPasswordModel

data class ForgotPasswordModel(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)