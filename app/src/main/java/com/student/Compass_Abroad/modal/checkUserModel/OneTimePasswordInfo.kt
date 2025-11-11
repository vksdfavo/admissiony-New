package com.student.Compass_Abroad.modal.checkUserModel

data class OneTimePasswordInfo(
    val identifier: String,
    val otp: String,
    val send_to: String,
    val p_set: Boolean,
    val v_set: Boolean
)