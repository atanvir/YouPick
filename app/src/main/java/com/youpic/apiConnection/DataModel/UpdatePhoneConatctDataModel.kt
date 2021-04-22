package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class UpdatePhoneConatctDataModel(
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
)