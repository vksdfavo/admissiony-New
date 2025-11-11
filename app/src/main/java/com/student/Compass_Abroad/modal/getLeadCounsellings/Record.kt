package com.student.Compass_Abroad.modal.getLeadCounsellings

data class Record(
    val assigned_role_id: Int,
    val assigned_user_id: Int,
    val assigned_user_info: AssignedUserInfo,
    val created_at: String,
    val created_user_id: Int,
    val created_user_info: CreatedUserInfo,
    val deleted_at: Any,
    val destination_country: String,
    val dynamic_lead: DynamicLead,
    val id: Int,
    val identifier: String,
    val lead_id: Int,
    val managed_role_id: Int,
    val managed_user_id: Int,
    val managed_user_info: ManagedUserInfo,
    val meet_link: String,
    val remark_id: Int,
    val schedule_at: String,
    val status: String,
    val updated_at: String
)