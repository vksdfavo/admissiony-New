package com.student.Compass_Abroad.modal.ambassadroChatList

data class Record(
    val ambassadorInfo: AmbassadorInfo,
    val ambassador_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val lastMessage: List<LastMessage>,
    val relation_identifier: String,
    val updated_at: String,
    val userInfo: UserInfo,
    val user_id: Int
)