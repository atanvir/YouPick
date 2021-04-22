package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName

data class UserUpdateProfileDataModel(
        @SerializedName("data")
        var `data`: String,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
)