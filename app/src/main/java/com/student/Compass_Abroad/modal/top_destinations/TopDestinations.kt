package com.student.Compass_Abroad.modal.top_destinations

data class TopDestinations(
    val `data`: List<Data>?= null,
    val message: String?= null,
    val statusCode: Int?= null,
    val statusInfo: StatusInfo?= null,
    val success: Boolean = false
)