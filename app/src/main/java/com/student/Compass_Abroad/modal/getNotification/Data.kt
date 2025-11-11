package com.student.Compass_Abroad.modal.getNotification

data class Data(
    val hasUnreadNotifications: Boolean,
    val recordsInfo: List<RecordsInfo>,
    val unReadNotificationCount: Int
)