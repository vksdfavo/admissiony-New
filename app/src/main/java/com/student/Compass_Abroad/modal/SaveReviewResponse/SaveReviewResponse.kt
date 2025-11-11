package com.student.Compass_Abroad.modal.SaveReviewResponse

data class SaveReviewResponse(
    var `data`: Data? = null,
    var message: String? = "",
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)