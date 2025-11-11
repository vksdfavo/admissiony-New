package com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount

data class RecordsInfo(
    val agent_id: Int,
    val agent_info: AgentInfo,
    val country_id: Int,
    val created_at: String,
    val id: Int,
    val identifier: String,
    val info: List<Info>,
    val is_primary: Int,
    val role_info: RoleInfo,
    val total_invoices: Int,
    val total_paid_invoices: Int,
    val updated_at: String,
    val user_info: UserInfo
)