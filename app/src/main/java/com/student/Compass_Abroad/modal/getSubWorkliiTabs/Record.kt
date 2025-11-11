package com.student.Compass_Abroad.modal.getSubWorkliiTabs

data class Record(
    val assigned_to_info: AssignedToInfo,
    val assigned_to_role_info: AssignedToRoleInfo,
    val category: String,
    val completed_at: Any,
    val created_at: String,
    val created_by_info: CreatedByInfo,
    val created_by_role_info: CreatedByRoleInfo,
    val identifier: String,
    val is_automatic: Boolean,
    val lead_identifier: String,
    val module_id: Int,
    val module_identifier: String,
    val module_type: String,
    val remarkInfo: RemarkInfo,
    val scheduled_at: String,
    val status: String,
    val title: String,
    val type: String,
    val updated_at: String
)