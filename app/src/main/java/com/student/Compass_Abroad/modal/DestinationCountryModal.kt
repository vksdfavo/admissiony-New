package com.student.Compass_Abroad.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DestinationCountryModal {
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("statusInfo")
    @Expose
    var statusInfo: StatusInfo? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    class Datum {
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("short_name")
        @Expose
        var shortName: String? = null

        @SerializedName("lead_count")
        @Expose
        var leadCount: Int? = null

        @SerializedName("lead_converted_count")
        @Expose
        var leadConvertedCount: Int? = null

        @SerializedName("converted_count")
        @Expose
        var convertedCount: Int? = null
    }

    class StatusInfo {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("statusCode")
        @Expose
        var statusCode: Int? = null

        @SerializedName("statusMessage")
        @Expose
        var statusMessage: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null
    }
}