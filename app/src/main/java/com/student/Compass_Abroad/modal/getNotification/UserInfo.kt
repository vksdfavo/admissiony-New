package com.student.Compass_Abroad.modal.getNotification

import com.student.Compass_Abroad.modal.getNotification.AgentInfo

data class UserInfo(
    val agentInfo: AgentInfo,
    val first_name: String,
    val id: Int,
    val identifier: String,
    val last_name: String,
    val profile_picture_url: Any
)