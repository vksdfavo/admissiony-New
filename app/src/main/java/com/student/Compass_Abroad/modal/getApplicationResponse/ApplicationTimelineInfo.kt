package com.student.Compass_Abroad.modal.getApplicationResponse

data class ApplicationTimelineInfo(
    val actual_date: String,
    val applicationTimelineHoldInfo: Any,
    val applicationTimelineStatusInfo: ApplicationTimelineStatusInfo,
    val est_date: String,
    val est_days: Int,
    val status: String
)