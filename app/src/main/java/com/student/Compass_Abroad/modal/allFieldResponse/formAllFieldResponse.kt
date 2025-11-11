package com.student.Compass_Abroad.modal.allFieldResponse

data class formAllFieldResponse(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)