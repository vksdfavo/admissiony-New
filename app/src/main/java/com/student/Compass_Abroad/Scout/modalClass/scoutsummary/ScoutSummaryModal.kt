package com.student.Compass_Abroad.Scout.modalClass.scoutsummary

data class ScoutSummaryModal(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val success: Boolean
)