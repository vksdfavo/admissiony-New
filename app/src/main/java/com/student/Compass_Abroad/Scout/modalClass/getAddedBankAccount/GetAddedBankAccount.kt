package com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount

data class GetAddedBankAccount(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val success: Boolean
)