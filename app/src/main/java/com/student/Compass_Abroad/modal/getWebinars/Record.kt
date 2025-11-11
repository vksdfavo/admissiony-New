package com.student.Compass_Abroad.modal.getWebinars

data class Record(
    val attendeesCount: Int,
    val created_at: String,
    val deleted_at: Any,
    val description: String,
    val end_date: String,
    val event_detail: String,
    val event_title: String,
    val event_type: String,
    val fileInfo: FileInfo,
    val file_id: Int,
    val host: String,
    val id: Int,
    val identifier: String,
    val start_date: String,
    val status: String,
    val updated_at: String
)