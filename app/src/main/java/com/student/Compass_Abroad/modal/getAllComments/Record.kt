package com.student.Compass_Abroad.modal.getAllComments

data class Record(
    var commentReplyCount: Int,
    val content: String,
    val content_key: String,
    val created_at: String,
    val identifier: String,
    val status: String,
    val userInfo: UserInfo
)