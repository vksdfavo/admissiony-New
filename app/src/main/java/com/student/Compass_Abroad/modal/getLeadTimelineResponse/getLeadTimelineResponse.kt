package com.student.Compass_Abroad.modal.getLeadTimelineResponse

data class getLeadTimelineResponse(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)