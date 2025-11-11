package com.student.Compass_Abroad.modal.likePost

data class LikeResponse(
    var `data`: String? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)