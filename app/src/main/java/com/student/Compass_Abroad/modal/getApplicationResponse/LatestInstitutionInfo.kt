package com.student.Compass_Abroad.modal.getApplicationResponse

data class LatestInstitutionInfo(
    val institution_data: InstitutionData,
    val is_applied: Int,
    val institution_id: Int,
    val campus_id: Int,
)