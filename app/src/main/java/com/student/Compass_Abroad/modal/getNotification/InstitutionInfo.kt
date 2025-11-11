package com.student.Compass_Abroad.modal.getNotification

import com.student.Compass_Abroad.modal.getNotification.InstitutionData

data class InstitutionInfo(
    val campus_id: Int,
    val id: Int,
    val institution_data: InstitutionData,
    val is_applied: Int
)