package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class ActiveUserIdentityInfo(
    val group_info: GroupInfo,
    val id: Int,
    val identifier: String,
    val name: String
) : Serializable