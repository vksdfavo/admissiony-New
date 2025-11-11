package com.student.Compass_Abroad.Scout.modalClass.getBankForm

data class GetBankAccountForm(
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val data: BankAccountData,
    val message: String,
    val success: Boolean
)

data class StatusInfo(
    val id: String,
    val statusCode: Int,
    val statusMessage: String,
    val description: String,
    val category: String
)

data class BankAccountData(
    val form_identifier: String,
    val fields: List<BankField>
)

data class BankField(
    val name: String,
    val type: String,
    val label: String,
    val identifier: String,
    val is_required: Int,
    val options_data: Any?, // If this is a known object, replace with actual type
    val bank_information_form_field: BankInformationFormField
)

data class BankInformationFormField(
    val column: String,
    val step_no: Int,
    val order_by: Int,
    val is_hidden: Int,
    val is_required: Int,
    val placeholder: String,
    val validations: String,
    val default_value: Any? // Can be String? if always string or nullable type
)
