package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class test2(
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("deleteStatus")
    var deleteStatus: Boolean,
    @SerializedName("_id")
    var id: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("maxValue")
    var maxValue: String,
    @SerializedName("minValue")
    var minValue: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("__v")
    var v: String
)