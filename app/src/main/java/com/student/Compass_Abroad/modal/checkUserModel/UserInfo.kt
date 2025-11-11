package com.student.Compass_Abroad.modal.checkUserModel

data class UserInfo(
    val first_name: String,
    val identifier: String,
    val last_name: String,
    val profile_picture_url: String,
    val hash_one: String,
    val hash_two: String
)