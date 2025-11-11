package com.student.Compass_Abroad.modal.saveApplicationDocuments

data class DocumentTypeInfo(
    val created_at: String,
    val created_by_role_id: Int,
    val created_by_user_id: Int,
    val deleted_at: Any,
    val id: Int,
    val identifier: String,
    val is_default: Int,
    val module: String,
    val name: String,
    val updated_at: String
)