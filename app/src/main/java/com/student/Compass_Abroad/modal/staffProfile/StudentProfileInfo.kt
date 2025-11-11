package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class StudentProfileInfo(
    val assignedStaffInfo: AssignedStaffInfo,
    val identifier: String,
    val student_id: Int
) : Serializable