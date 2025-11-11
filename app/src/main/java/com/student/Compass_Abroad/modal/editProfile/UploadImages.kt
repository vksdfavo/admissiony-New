package com.student.Compass_Abroad.modal.editProfile

data class UploadImages(
    val `data`: DataX? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfoX? = null,
    val success: Boolean = false
)