package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class CheckMobilForForgotPasswordDataModel(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
        @SerializedName("otp")
        var otp: String,
        @SerializedName("userId")
        var userId: String
    )
}