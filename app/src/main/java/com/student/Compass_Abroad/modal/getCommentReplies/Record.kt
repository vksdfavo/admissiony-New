package com.student.Compass_Abroad.modal.getCommentReplies

data class Record(
    val commentReplyCount: Int,
    val content: String,
    val content_key: String,
    val created_at: String,
    val identifier: String,
    val status: String,
    val userInfo: UserInfo
)