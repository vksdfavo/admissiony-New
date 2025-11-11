package com.student.Compass_Abroad.modal.documentType


data class DocumentTypeModal(
    val statusCode: Int? = null,
    val statusInfo: StatusInfo?? = null,
    val data: Data? = null,
    val message: String? = null,
    val success: Boolean = false
)

data class Data(
    val recordsInfo: List<RecordsInfo>?
)

data class RecordsInfo(
    val label: String?,
    val value: String?
)

data class StatusInfo(
    val id: String?,
    val statusCode: Int?,
    val statusMessage: String?,
    val description: String?,
    val category: String?
)


