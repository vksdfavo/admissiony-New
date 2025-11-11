package com.student.Compass_Abroad.modal.changeStatusReminder

data class changeStatusReminder(
    var `data`: String? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)