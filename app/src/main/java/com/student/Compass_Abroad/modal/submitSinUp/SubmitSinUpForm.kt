package com.student.Compass_Abroad.modal.submitSinUp

import org.json.JSONArray


data class SubmitSinUpForm(
    var statusCode: Int=0,
    var statusInfo: StatusInfo?=null,
    var data: Data?=null,
    var message: String?=null,
    var success: Boolean?=false,
    var errors: JSONArray? = null
)

data class StatusInfo(
    val id: String,
    val statusCode: Int,
    val statusMessage: String,
    val description: String,
    val category: String
)

data class Data(
    val userInfo: UserInfo,
    val publicInfo: PublicInfo
)

data class UserInfo(
    val identifier: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val country_code: Int,
    val mobile: Long,
    val hash_one: String,
    val hash_two: String
)

data class PublicInfo(
    val p_set: Boolean,
    val v_set: Boolean
)
