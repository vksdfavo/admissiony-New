package com.student.Compass_Abroad.modal.getNotification

data class RecordsInfo(
    val created_at: String,
    val delivered_at: String,
    val delivery_status: String,
    val id: Int,
    val notification_info: NotificationInfo,
    val open_status: String,
    val opened_at: String,
    val read_at: String,
    val read_status: String,
    val recipient_info: RecipientInfo,
    val sent_at: String,
    val sent_template_info: SentTemplateInfo,
    val status: String,
    val updated_at: String
)