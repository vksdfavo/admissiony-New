package com.student.Compass_Abroad.modal.getNotification

data class NotificationInfo(
    val content: String,
    val id: Int,
    val identifier: String,
    val module_id: Int,
    val module_info: ModuleInfo,
    val module_type: String,
    val status: String,
    val title: String,
    val type: String,
    val variables: List<Variable>
)