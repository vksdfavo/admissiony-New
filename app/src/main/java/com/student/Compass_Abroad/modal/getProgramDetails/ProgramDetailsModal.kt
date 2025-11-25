package com.student.Compass_Abroad.modal.getProgramDetails

data class ProgramDetailsModal(
    val `data`: Data?=null,
    val message: String?=null,
    val statusCode: Int?=null,
    val statusInfo: StatusInfoX?=null,
    val success: Boolean = false
)