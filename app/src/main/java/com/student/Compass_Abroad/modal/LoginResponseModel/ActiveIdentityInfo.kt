package com.student.Compass_Abroad.modal.LoginResponseModel

data class ActiveIdentityInfo(
    val group_info: GroupInfo,
    val id: Int,
    val identifier: String,
    val name: String
)