package com.student.Compass_Abroad.modal.getLeadAssignedStaffResponse

data class getLeadAssignedStaffResponse(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)