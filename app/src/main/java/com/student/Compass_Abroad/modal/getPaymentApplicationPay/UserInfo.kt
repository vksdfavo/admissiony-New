package com.student.Compass_Abroad.modal.getPaymentApplicationPay

data class UserInfo(
    val country_code: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val mobile: Long,
    val profile_picture_url: String
)