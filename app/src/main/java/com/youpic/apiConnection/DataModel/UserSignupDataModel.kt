package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName


data class UserSignupDataModel(
        @SerializedName("data")
        var data: Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("countryCode")
            var countryCode: String,
            @SerializedName("createdAt")
            var createdAt: String,
            @SerializedName("deleteStatus")
            var deleteStatus: Boolean,
            @SerializedName("deviceToken")
            var deviceToken: String,
            @SerializedName("deviceType")
            var deviceType: String,
            @SerializedName("giveRating")
            var giveRating: Boolean,
            @SerializedName("_id")
            var id: String,
            @SerializedName("isRate")
            var isRate: Boolean,
            @SerializedName("jwtToken")
            var jwtToken: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("location")
            var location: Location,
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("mobileNumber")
            var mobileNumber: String,
            @SerializedName("name")
            var name: String,
            @SerializedName("notificationStatus")
            var notificationStatus: Boolean,
            @SerializedName("phoneContact")
            var phoneContact: List<Any>,
            @SerializedName("pickFavorites")
            var pickFavorites: List<String>,
            @SerializedName("points")
            var points: Int,
            @SerializedName("profilePic")
            var profilePic: String,
            @SerializedName("soundStatus")
            var soundStatus: Boolean,
            @SerializedName("status")
            var status: String,
            @SerializedName("timezone")
            var timezone: String,
            @SerializedName("updatedAt")
            var updatedAt: String,
            @SerializedName("userType")
            var userType: String,
            @SerializedName("__v")
            var v: Int
    ) {
        data class Location(
                @SerializedName("coordinates")
                var coordinates: List<Double>,
                @SerializedName("type")
                var type: String
        )
    }
}
