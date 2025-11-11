package com.student.Compass_Abroad.modal.LoginResponseModel

data class StatusInfo(
    val category: String,
    val description: String,
    val id: String,
    val statusCode: Int,
    val statusMessage: String
)