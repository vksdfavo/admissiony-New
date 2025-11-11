package com.student.Compass_Abroad.modal.getWorkliiTabs

data class TabsInfo(
    val all_task: Int,
    val assigned_tasks: Int,
    val future_tasks: Int,
    val info_tasks: Int,
    val past_tasks: Int,
    val reminder_tasks: Int,
    val response_tasks: Int,
    val today_tasks: Int,
    val transferred_tasks: Int
)