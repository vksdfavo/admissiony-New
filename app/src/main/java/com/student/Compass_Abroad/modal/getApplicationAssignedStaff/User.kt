package com.student.Compass_Abroad.modal.getApplicationAssignedStaff

data class User(
    val identifier: String,
    val country_code: Int,
    val first_name: String,
    val last_name: String,
    val mobile: Long,
    val profile_picture_url: String,
    val average_rating: Float,
    val total_applications_assigned: Int,
    val is_reviewed: Int
)