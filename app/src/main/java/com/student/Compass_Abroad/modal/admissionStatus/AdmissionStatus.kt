package com.student.Compass_Abroad.modal.admissionStatus

data class AdmissionStatus(
    val data: List<Data>?,                 // made nullable
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo?,          // made nullable
    val success: Boolean
)
