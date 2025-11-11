package com.student.Compass_Abroad.modal.getApplicationRemider

data class getApplicationReminderResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)