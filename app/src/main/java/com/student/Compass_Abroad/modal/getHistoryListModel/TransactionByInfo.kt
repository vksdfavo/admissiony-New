package com.student.Compass_Abroad.modal.getHistoryListModel

data class TransactionByInfo(
    val agentInfo: AgentInfo,
    val email: String,
    val first_name: String,
    val identifier: String,
    val last_name: String,
    val mobile: Long,
    val profile_picture_url: String
)