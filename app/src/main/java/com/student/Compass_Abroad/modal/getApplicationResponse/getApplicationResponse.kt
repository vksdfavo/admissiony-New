package com.student.Compass_Abroad.modal.getApplicationResponse

data class getApplicationResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfoX? = null,
    var success: Boolean = false
)