package com.student.Compass_Abroad.fragments

import com.google.gson.annotations.SerializedName

data class SubmitRequest (
    @SerializedName("data") val data: Map<String, Any?>,
    @SerializedName("form_identifier") val form_identifier: String
)