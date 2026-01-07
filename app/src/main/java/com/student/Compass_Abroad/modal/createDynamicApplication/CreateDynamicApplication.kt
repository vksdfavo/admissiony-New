package com.student.Compass_Abroad.modal.createDynamicApplication

data class CreateDynamicApplication(
    val statusCode: Int,
    val statusInfo: StatusInfo?,
    val data: FormData?,
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

data class FormData(
    val id: Int,
    val identifier: String,
    val name: String,
    val description: String,
    val module_type: String,
    val status: String,
    val operation: String,
    val is_active: Int,
    val has_step: Int,
    val step_count: Int,
    val fields: Map<String, List<FormField>>
)

data class FormField(
    val id: Int,
    val name: String,
    val type: String,
    val label: String,
    val column: Int,
    val order_by: Int,
    val is_hidden: Int,
    val identifier: String,
    val is_readonly: Int,
    val placeholder: String,
    val validations: Validations,
    val field_options: FieldOptions?,
    val is_filterable: Int,
    val is_searchable: Int,
    val conditionally_visible: Int,
    val assigned_field_options: AssignedFieldOptions
)

data class Validations(
    val name: String,
    val type: String,
    val required: Boolean
)

data class FieldOptions(
    val url: String? = null,
    val type: String? = null,
    val params: List<ApiParam>? = null,
    val is_already_param_exist: Boolean? = null,
    val options: List<Map<String, Any>>? = null  // Added for manual options
)

data class ApiParam(
    val name: String,
    val required: Boolean,
    val form_field_key: String? = null
)

data class AssignedFieldOptions(
    val params: List<ApiParam>? = null,
    val has_children: Boolean,
    val is_dependent: Boolean,
    val children_fields: List<String>,
    val parent_field_name: String? = null
)