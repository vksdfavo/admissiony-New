package com.student.Compass_Abroad.modal.getLeadsDocuments

data class File(
    val created_at: String,
    val download_page: String,
    val file_extension: String,
    val filename: String,
    val identifier: String,
    val thumb_info: ThumbInfo,
    val view_page: String
)