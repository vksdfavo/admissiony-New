package com.student.Compass_Abroad.modal.paymentDetails

data class TransactionByInfo(
    val agentInfo: AgentInfo,
    val first_name: String,
    val identifier: String,
    val last_name: String,
    val profile_picture_url: String
)