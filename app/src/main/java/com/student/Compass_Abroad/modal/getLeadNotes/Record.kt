package com.student.Compass_Abroad.modal.getLeadNotes

data class Record(
    val id: Int,
    val identifier: String,
    val title: String,
    val content: String,
    val content_key: String,
    val color: String,
    val is_remark: Boolean,
    val is_public: Boolean,
    val created_by_id: Int,
    val created_by_role_id: Int,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val created_by_info: CreatedByInfo,
    val created_by_role_info: CreatedByRoleInfo
)