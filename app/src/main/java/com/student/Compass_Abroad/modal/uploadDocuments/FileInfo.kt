package com.student.Compass_Abroad.modal.uploadDocuments

data class FileInfo(
    val created_at: String,
    val created_by_role_id: Int,
    val created_by_user_id: Int,
    val device_id: Int,
    val download_page: String,
    val file_extension: String,
    val file_mime_type: String,
    val file_path: String,
    val file_type: String,
    val filename: String,
    val filename_server: String,
    val id: Int,
    val identifier: String,
    val is_public: Int,
    val server_id: Int,
    val thumbnail_file_id: Int,
    val updated_at: String,
    val view_page: String
)