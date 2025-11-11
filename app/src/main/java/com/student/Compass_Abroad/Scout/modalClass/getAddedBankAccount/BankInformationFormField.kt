package com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount

data class BankInformationFormField(
    val column: String,
    val default_value: Any,
    val is_hidden: Int,
    val is_required: Int,
    val order_by: Int,
    val placeholder: String,
    val step_no: Int,
    val validations: String
)