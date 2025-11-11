package com.student.Compass_Abroad.modal.CreateApplication

data class CreateApplicationModal(
    val `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfoX? = null,
    val success: Boolean = false
)