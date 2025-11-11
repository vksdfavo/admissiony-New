package com.student.Compass_Abroad.modal.getApplicationResponse

data class LeadInfo(
    val birthday: String,
    val city: String,
    val country: String,
    val country_code: String,
    val created_at: String,
    val destination_country: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val identifier: String,
    val last_name: String,
    val lead_source_id: Int,
    val lead_stage: LeadStage,
    val lead_stage_id: Int,
    val mobile: String,
    val state: String,
    val user: User,
    val user_profile_id: Int,
    val utm_param: UtmParam
)