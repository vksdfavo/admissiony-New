package com.student.Compass_Abroad.newdynamicapi

data class SubmitPayload(
    val form_identifier: String,
    val data: Map<String, @JvmSuppressWildcards Any>
)
