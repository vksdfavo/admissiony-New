package com.student.Compass_Abroad.modal.postLeadStatus

data class postLeadStatus(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)