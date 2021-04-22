package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetCuisineListDataModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("deleteStatus")
        var deleteStatus: Boolean,
        @SerializedName("_id")
        var id: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("status")
        var status: String,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("__v")
        var v: String,
        var isSelected:Boolean
    )
}