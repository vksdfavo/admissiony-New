package com.student.Compass_Abroad.modal.postApplicationNotes

data class Data(
    val color: String,
    val content: String,
    val content_key: String,
    val created_at: String,
    val created_by_id: Int,
    val created_by_role_id: Int,
    val id: Int,
    val identifier: String,
    val is_public: Boolean,
    val is_remark: Boolean,
    val title: String,
    val updated_at: String
)