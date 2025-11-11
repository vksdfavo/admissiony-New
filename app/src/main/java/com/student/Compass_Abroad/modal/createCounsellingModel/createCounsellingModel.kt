package com.student.Compass_Abroad.modal.createCounsellingModel

data class createCounsellingModel(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)