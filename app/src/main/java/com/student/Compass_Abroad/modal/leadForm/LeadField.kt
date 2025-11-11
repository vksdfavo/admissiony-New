package com.student.Compass_Abroad.modal.leadForm

data class LeadField(
    val identifier: String,
    val label: String,
    val lead_form_field: LeadFormField,
    var name: String,
    val options_data: OptionsData,
    val type: String
)