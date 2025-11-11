package com.student.Compass_Abroad.modal.ambassadorGetChat

data class AmbassadorGetChatData(
    val `data`: Data?=null,
    var message: String?=null,
    var statusCode: Int?=null,
    val statusInfo: StatusInfo?=null,
    val success: Boolean = false
)