package com.student.Compass_Abroad.modal.getVoucherModel

data class Record(
    val available_voucher: Int,
    val base_amount: Int,
    val base_currency: String,
    val created_at: String,
    val description: String,
    val display_amount: Int,
    val display_currency: String,
    val file_url: String,
    val identifier: String,
    val name: String,
    val quantity_limit: Int,
    val sold_voucher: Int,
    val status: Int,
    val updated_at: String,
    val voucherTypeInfo: VoucherTypeInfo,
    val voucher_type_id: Int
)