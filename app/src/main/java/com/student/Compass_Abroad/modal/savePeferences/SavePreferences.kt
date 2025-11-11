package com.student.Compass_Abroad.modal.savePeferences

data class SavePreferences(
    val `data`: String? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)