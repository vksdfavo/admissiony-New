package com.student.Compass_Abroad.modal.getNotification

import com.student.Compass_Abroad.modal.getNotification.CategoryInfo
import com.student.Compass_Abroad.modal.getNotification.ChannelInfo

data class NotificationTemplateInfo(
    val category_id: Int,
    val category_info: CategoryInfo,
    val channel_id: Int,
    val channel_info: ChannelInfo,
    val id: Int,
    val is_fallback: Int
)