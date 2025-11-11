package com.student.Compass_Abroad.modal.getApplicationTimelineResponse

data class Data(
    val actual_date: String,
    val applicationTimelineHoldInfo: ApplicationTimelineHoldInfo,
    val applicationTimelineStatusInfo: ApplicationTimelineStatusInfo,
    val est_date: String,
    val est_days: Int,
    val status: String
)