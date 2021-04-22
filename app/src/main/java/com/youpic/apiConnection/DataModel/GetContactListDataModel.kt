package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetContactListDataModel(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
            var isSelected:Boolean,
        @SerializedName("countryCode")
        var countryCode: String,
        @SerializedName("isExist")
        var isExist: Boolean,
        @SerializedName("isFriend")
        var isFriend: Boolean,
        @SerializedName("mobileNumber")
        var mobileNumber: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("isRequest")
        var isRequest: Boolean,

    )
}