package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetFriendListDataModel(
    @SerializedName("data")
    var `data`: ArrayList<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
        @SerializedName("acceptBy")
        var acceptBy: String,
        @SerializedName("acceptByUserData")
        var acceptByUserData: AcceptByUserData,
        @SerializedName("acceptTo")
        var acceptTo: String,
        @SerializedName("acceptToData")
        var acceptToData: AcceptToData,
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("_id")
        var id: String
    ) {
        data class AcceptByUserData(
            @SerializedName("_id")
            var id: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("name")
            var name: String,
            @SerializedName("profilePic")
            var profilePic: String
        )

        data class AcceptToData(
            @SerializedName("_id")
            var id: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("name")
            var name: String,
            @SerializedName("profilePic")
            var profilePic: String
        )
    }
}