package com.student.Compass_Abroad.modal.createDynamicApplication.dropdown

data class DropdownResponse(
    val data: Any? = null,               // can be List or Object
    val message: String? = null,
    val statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)

data class Data(
    val label: String,
    val value: Int
)

data class StatusInfo(
    val category: String,
    val description: String,
    val id: String,
    val statusCode: Int,
    val statusMessage: String
)