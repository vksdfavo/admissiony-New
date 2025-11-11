package com.student.Compass_Abroad.Scout.modalClass.primaryAccount

data class MakePrimaryAccount(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val success: Boolean
)