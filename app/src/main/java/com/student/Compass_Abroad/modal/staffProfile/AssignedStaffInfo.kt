package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class AssignedStaffInfo(
    val country_code: Int,
    val first_name: String,
    val identifier: String,
    val last_name: String,
    val email: String,
    val mobile: Long,
    val profile_picture_url: String
) : Serializable