package com.student.Compass_Abroad.modal.createPostResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CreatePostResponse {
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("statusInfo")
    @Expose
    var statusInfo: Data.StatusInfo? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    class Data {

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
}

