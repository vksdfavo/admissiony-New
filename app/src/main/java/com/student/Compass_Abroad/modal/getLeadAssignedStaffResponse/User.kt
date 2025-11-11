package com.student.Compass_Abroad.modal.getLeadAssignedStaffResponse

data class User(
    val country_code: Int,
    val first_name: String,
    val last_name: String,
    val mobile: Long,
    val profile_picture_url: String
)