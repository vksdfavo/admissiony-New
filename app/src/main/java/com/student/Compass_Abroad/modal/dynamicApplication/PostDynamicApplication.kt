package com.student.Compass_Abroad.modal.dynamicApplication

data class PostDynamicApplication(
    val `data`: Data? = null,
    val message: String? = null,
    val statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)