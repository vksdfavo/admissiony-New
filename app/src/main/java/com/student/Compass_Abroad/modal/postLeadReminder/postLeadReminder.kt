package com.student.Compass_Abroad.modal.postLeadReminder

data class postLeadReminder(
    var `data`: Any? = null,
    var errors: List<Error>? = null,
    var statusCode: Int = 0,
    var message: String? = null,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)