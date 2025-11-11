package com.student.Compass_Abroad.Scout.modalClass

import com.google.gson.annotations.SerializedName


data class BankAccountRequest(
    @SerializedName("country_identifier")
    val countryIdentifier: String,

    @SerializedName("is_primary")
    val isPrimary: Boolean? = null, // Optional

    val info: BankInfoRequest
)

data class BankInfoRequest(
    @SerializedName("account_name")
    val accountName: String,

    @SerializedName("account_number")
    val accountNumber: String,

    @SerializedName("bank_name")
    val bankName: String,

    @SerializedName("bank_address")
    val bankAddress: String,

    @SerializedName("swift_code")
    val swiftCode: String
)
