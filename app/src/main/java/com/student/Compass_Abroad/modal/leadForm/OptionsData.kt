package com.student.Compass_Abroad.modal.leadForm

data class OptionsData(
    val can_select_multiple: Boolean,
    val dependent_field_name: String,
    val fields: List<String>,
    val format: Format,
    val is_already_param_exist: Boolean,
    val is_dependent: Boolean,
    val options: List<Option>,
    val param_info: List<ParamInfo>,
    val related_fields: List<String>,
    val type: String,
    val url: String


)