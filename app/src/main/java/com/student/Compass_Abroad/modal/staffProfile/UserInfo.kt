package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class UserInfo(
    val birthday: String,
    val country_code: Int,
    val created_at: String,
    val default_role_id: Int,
    val deleted_at: Any,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val gender: String,
    val group: String,
    val id: Int,
    val identifier: String,
    val identityInfo: List<IdentityInfo>,
    val last_activity_at: String,
    val last_login_at: String,
    val last_name: String,
    val marital_status: String,
    val mobile: Long,
    val mobile_verified_at: String,
    val profile_picture_url: String,
    val status: String,
    val updated_at: String
) : Serializable