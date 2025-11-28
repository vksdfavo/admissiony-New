package com.student.Compass_Abroad.modal.getProgramDetails

data class Institution(
    val commission_currency: Any,
    val country: Country,
    val country_id: Int,
    val id: Int,
    val identifier: String,
    val is_firmli_public: Int,
    val is_public: Int,
    val logo: String,
    val name: String,
    val url: String
)