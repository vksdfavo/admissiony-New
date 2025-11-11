package com.student.Compass_Abroad.modal.getReviewList

data class getReviewList(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)