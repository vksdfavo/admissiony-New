package com.student.Compass_Abroad.modal.getAllPosts

data class Record(
    val category: Category,
    val commentCount: Int,
    val content: String,
    val content_key: String,
    val created_at: String,
    val identifier: String,
    val reactions: List<Reaction>,
    val userInfo: UserInfo
)