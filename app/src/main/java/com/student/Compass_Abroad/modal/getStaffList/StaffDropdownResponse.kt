package com.student.Compass_Abroad.modal.getStaffList

data class StaffDropdownResponse(
    val statusCode: Int?= null,
    val statusInfo: StatusInfo?= null,
    val data: StaffData?= null,
    val message: String?= null,
    val success: Boolean = false
)

data class StatusInfo(
    val id: String?,
    val statusCode: Int?,
    val statusMessage: String?,
    val description: String?,
    val category: String?
)

data class StaffData(
    val recordsInfo: List<RecordsInfo>?
)

data class RecordsInfo(
    val value: String?,
    val label: String?
)
