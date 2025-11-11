package com.student.Compass_Abroad


    data class ApiResponseForm(
        var statusCode: Int?=null,
        val statusInfo: StatusInfo?=null,
        val data: Data?=null,
        var message: String?=null,
        val success: Boolean = false
    )

    data class StatusInfo(
        val id: String,
        val statusCode: Int,
        val statusMessage: String,
        val description: String,
        val category: String
    )

    data class Data(
        val identifier: String,
        val name: String,
        val description: String,
        val has_step: Int,
        val step_count: Int,
        val is_default: Int,
        val is_primary: Int,
        val status: String,
        val lead_fields: List<LeadField>?
    )

    data class LeadField(
        val identifier: String,
        val name: String,
        val is_required: Int,
        val label: String,
        val type: String,
        val options_data: OptionsData?,
        val lead_form_field: LeadFormField
    )

    data class OptionsData(
        val url: String?,
        val type: String?,
        val format: Format?,
        val is_dependent: Boolean?,
        val related_fields: List<String>?,
        val can_select_multiple: Boolean?,
        val dependent_field_name: String?,
        val param_info: List<ParamInfo>?,
        val is_already_param_exist: Boolean?,
        val options: List<Option>?
    )

    data class Option(
        val label: String,
        val value: String,
        val is_selected: Boolean
    )


    data class Format(
        val label: String,
        val value: String
    )

    data class ParamInfo(
        val field_name: String,
        val param_name: String
    )

    data class LeadFormField(
        val validations: String,
        val column: String,
        val placeholder: String,
        val default_value: String?,
        val is_required: Int,
        val is_hidden: Int,
        val step_no: Int,
        val order_by: Int
    )