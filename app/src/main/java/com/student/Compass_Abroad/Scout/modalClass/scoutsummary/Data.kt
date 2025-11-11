package com.student.Compass_Abroad.Scout.modalClass.scoutsummary

data class Data(
    val actual_payout_amount: Int,
    val actual_payout_currency: String,
    val applications_count: Int,
    val converted_leads_count: Int,
    val leads_count: Int,
    val potential_payout_amount: Int,
    val potential_payout_currency: String
)