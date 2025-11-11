package com.student.Compass_Abroad.modal.createCounsellingModel

data class Data(
    val counseling_type: String,
    val created_user_id: Int,
    val destination_country: String,
    val identifier: String,
    val lead_id: Int,
    val lead_identifier: String,
    val managed_identifier: String,
    val managed_role_id: Int,
    val managed_user_id: Int,
    val meet_link: String,
    val remark: String,
    val remark_id: Int,
    val schedule_at: String
)