package com.student.Compass_Abroad.modal.getLeadTimelineResponse

data class Data(
    val created_at: String,
    val is_disabled: Int,
    val lead_stage_name: String,
    val lead_stage_short_name: String,
    val lead_timeline_identifier: String,
    val status: String
)