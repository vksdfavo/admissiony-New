package com.student.Compass_Abroad.modal.leadForm

data class Data(
    val description: String,
    val has_step: Int,
    val identifier: String,
    val is_default: Int,
    val is_primary: Int,
    val lead_fields: List<LeadField>,
    val name: String,
    val status: String,
    val step_count: Int
)