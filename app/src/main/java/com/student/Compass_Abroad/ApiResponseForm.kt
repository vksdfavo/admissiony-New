package com.student.Compass_Abroad

data class ApiResponseForm(
    var statusCode: Int=0,
    var statusInfo: StatusInfo?=null,
    var data: FormData?=null,
    var message: String?=null,
    var success: Boolean=false
)
data class StatusInfo(
    val id: String?,
    val statusCode: Int?,
    val statusMessage: String?,
    val description: String?,
    val category: String?
)
data class FormData(
    val id: Int?,
    val identifier: String?,
    val name: String?,
    val description: String?,
    val module_type: String?,
    val status: String?,
    val operation: String?,
    val is_active: Int?,
    val has_step: Int?,
    val step_count: Int?,
    val fields: Map<String, List<FormField>>? // "1": [ ... fields ... ]
)

data class FormField(
    val form_step_no: Int?,
    val id: Int?,
    val identifier: String?,
    val name: String,
    val label: String?,
    val placeholder: String,
    val assigned_field_options: AssignedFieldOptions?,
    val field_options: FieldOptions?,
    val validations: Validations?,
    val type: String?,
    val is_searchable: Int?,
    val is_filterable: Int?,
    val is_hidden: Int?,
    val is_readonly: Int?,
    val conditionally_visible: Int?,
    val column: Int?,
    val order_by: Int?
)

data class AssignedFieldOptions(
    val params: List<DependentParam>? = null,
    val has_children: Boolean? = false,
    val is_dependent: Boolean? = false,
    val children_fields: List<String>? = emptyList(),
    val parent_field_name: String? = null
)

data class FieldOptions(
    val url: String? = null,
    val type: String? = null,
    val params: List<FieldParam>? = null,
    val is_already_param_exist: Boolean? = null,
    val options: List<OptionItem>? = null
)

data class Validations(
    val name: String?,
    val type: String?,
    val required: Boolean?
)

data class DependentParam(
    val name: String?,
    val required: Boolean?,
    val form_field_key: String?
)

data class FieldParam(
    val name: String?,
    val required: Boolean?
)

data class OptionItem(
    val label: String,
    val value: String
)
