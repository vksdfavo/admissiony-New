package com.student.Compass_Abroad.modal.getApplicationDocuments

data class getApplicationDocuments(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)