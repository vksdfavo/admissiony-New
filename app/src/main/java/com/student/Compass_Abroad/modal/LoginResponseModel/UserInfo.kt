package com.student.Compass_Abroad.modal.LoginResponseModel

data class UserInfo(
    val birthday: Any,
    val country_code: Int,
    val created_at: String,
    val default_role_id: Any,
    val deleted_at: Any,
    val email: String,
    val email_verified_at: String,
    val first_name: String,
    val gender: String,
    val group: String,
    val identifier: String,
    val identityInfo: List<IdentityInfo>,
    val last_activity_at: String,
    val last_login_at: String,
    val last_name: String,
    val marital_status: String,
    val mobile: String,
    val mobile_verified_at: String,
    val profile_picture_url: Any,
    val status: String,
    val updated_at: String
)