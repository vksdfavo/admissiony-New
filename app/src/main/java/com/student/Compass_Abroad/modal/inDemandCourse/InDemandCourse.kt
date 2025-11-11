package com.student.Compass_Abroad.modal.inDemandCourse

data class InDemandCourse(
    val `data`: List<Data>? = null,
    val message: String? = null,
    val statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)