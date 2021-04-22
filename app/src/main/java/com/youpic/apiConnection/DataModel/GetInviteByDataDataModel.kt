package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetInviteByDataDataModel(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
        @SerializedName("_id")
        var id: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("profilePic")
        var profilePic: String
    )
}