package com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount

data class Info(
    val bank_information_form_field: BankInformationFormField,
    val identifier: String,
    val is_required: Int,
    val label: String,
    val name: String,
    val options_data: Any,
    val type: String,
    val value: String
)