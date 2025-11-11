package com.student.Compass_Abroad.modal.TokenFcmData

import com.student.Compass_Abroad.modal.TokenFcmData.Data
import com.student.Compass_Abroad.modal.TokenFcmData.StatusInfoX

data class TokenFcmData(
    val `data`: Data?=null,
    var message: String?=null,
    var statusCode: Int?=null,
    val statusInfo: StatusInfoX?=null,
    val success: Boolean = false
)
