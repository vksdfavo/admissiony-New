package com.student.Compass_Abroad.modal.refreshToken

data class RefreshTokenResonse(
    val `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)