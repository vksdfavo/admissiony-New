package com.student.Compass_Abroad.modal.leadForm

data class LeadFormField(
    val column: String,
    val default_value: Any,
    val is_hidden: Int,
    val order_by: Int,
    val placeholder: String,
    val step_no: Int,
    val validations: String
)