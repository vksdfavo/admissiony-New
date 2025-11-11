package com.student.Compass_Abroad.modal.clientEventModel

import java.io.Serializable

data class Record(
    val created_at: String,
    val deleted_at: String?,
    val destination_country_id: Int,
    val end_at: String?,
    val event_address: String?,
    val event_at: String,
    val event_for: String,
    val event_link: String,
    val event_type: String,
    val identifier: String,
    val name: String,
    val registration_link: String,
    val start_at: String?,
    val status: String,
    val timezone: String?,
    val updated_at: String
) : Serializable
