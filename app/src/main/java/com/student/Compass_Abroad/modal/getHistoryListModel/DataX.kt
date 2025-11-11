package com.student.Compass_Abroad.modal.getHistoryListModel

data class DataX(
    val bought_quantity: Int,
    val description: String,
    val file_url: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val voucher_type_info: VoucherTypeInfo
)