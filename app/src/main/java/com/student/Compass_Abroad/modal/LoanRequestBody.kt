package com.student.Compass_Abroad.modal

data class LoanRequestBody(
    val name: String,
    val email: String,
    val mobile_number: String,
    val amount: String,
    val target_country_code: String,
    val application_status: String
)
