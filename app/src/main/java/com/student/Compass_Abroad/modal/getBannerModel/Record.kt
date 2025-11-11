package com.student.Compass_Abroad.modal.getBannerModel

data class Record(
    val created_at: String,
    val fileInfo: FileInfo,
    val file_id: Int,
    val id: Int,
    val identifier: String,
    val isHide: Boolean,
    val name: String,
    val updated_at: String
)