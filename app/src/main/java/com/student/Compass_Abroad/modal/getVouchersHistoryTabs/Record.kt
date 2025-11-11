package com.student.Compass_Abroad.modal.getVouchersHistoryTabs

data class Record(
    val available_voucher: Int,
    val created_at: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val quantity_limit: Int,
    val sold_voucher: String,
    val updated_at: String,
    val voucherPaymentInfo: List<VoucherPaymentInfo>,
    val voucherTypeInfo: VoucherTypeInfo
)