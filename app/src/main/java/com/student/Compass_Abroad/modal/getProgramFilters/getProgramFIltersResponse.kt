package com.student.Compass_Abroad.modal.getProgramFilters


data class getProgramFIltersResponse(
    var `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)