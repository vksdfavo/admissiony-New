package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class Data(
    val activeUserIdentityInfo: ActiveUserIdentityInfo,
    val hasStudentProfile: Boolean,
    val studentProfileInfo: StudentProfileInfo,
    val userInfo: UserInfo
) : Serializable
