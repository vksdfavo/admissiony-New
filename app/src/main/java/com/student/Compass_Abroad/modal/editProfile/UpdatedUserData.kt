package com.student.Compass_Abroad.modal.editProfile

data class UpdatedUserData(
    val birthday: String,
    val country_code: Int,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val identifier: String,
    val last_name: String,
    val marital_status: String,
    val mobile: Int
)