package com.student.Compass_Abroad.modal.getBannerModel

data class GetBannerModal(
    val `data`: Data?=null,
    val message: String?=null,
    val statusCode: Int?=null,
    val statusInfo: StatusInfo?=null,
    val success: Boolean = false
)