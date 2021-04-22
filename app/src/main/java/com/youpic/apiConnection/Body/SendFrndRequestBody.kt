package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class SendFrndRequestBody(
    @SerializedName("requestData")
    var requestData: List<RequestData?>,

    @SerializedName("link")
    var link:String?
) {
    data class RequestData(
        @SerializedName("countryCode")
        var countryCode: String,
        @SerializedName("isExist")
        var isExist: Boolean,
        @SerializedName("mobileNumber")
        var mobileNumber: String

    )
}