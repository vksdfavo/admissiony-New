package com.student.Compass_Abroad.modal

data class ChangeLeadStatus(
    val `data`: List<Any>? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)