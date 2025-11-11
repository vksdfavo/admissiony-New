package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class IdentityInfo(
    val group_info: GroupInfoX,
    val identifier: String,
    val name: String
) : Serializable