package com.student.Compass_Abroad.modal.getLeadNotes

data class getLeadNotesResponse(
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var data: Data? = null,
    var message: String? = null,
    var success: Boolean = false
)