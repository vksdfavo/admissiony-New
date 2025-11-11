package com.student.Compass_Abroad.modal.in_demandInstitution

data class InDemandInstitution(
    val `data`: List<Data>?=null,
    val message: String?=null,
    val statusCode: Int?=null,
    val statusInfo: StatusInfo?=null,
    val success: Boolean = false
)