package com.student.Compass_Abroad.modal.ReportReasons

data class ReportReasonresponse(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)