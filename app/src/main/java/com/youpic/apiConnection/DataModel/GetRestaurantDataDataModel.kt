package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName

data class GetRestaurantDataDataModel(
        @SerializedName("data")
        var `data`: Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("actualId")
            var actualId: String,
            @SerializedName("address")
            var address: String,
            @SerializedName("adminImage")
            var adminImage: String,
            @SerializedName("city")
            var city: String,
            @SerializedName("country")
            var country: String,
            @SerializedName("createdAt")
            var createdAt: String,
            @SerializedName("cuisine")
            var cuisine: String,
            @SerializedName("cuisineArray")
            var cuisineArray: List<String>,
            @SerializedName("deleteStatus")
            var deleteStatus: Boolean,
            @SerializedName("email")
            var email: Any,
            @SerializedName("hours")
            var hours: Any,
            @SerializedName("_id")
            var id: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("location")
            var location: Location,
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("month")
            var month: Int,
            @SerializedName("name")
            var name: String,
            @SerializedName("phoneNumber")
            var phoneNumber: String,
            @SerializedName("postalCode")
            var postalCode: String,
            @SerializedName("state")
            var state: String,
            @SerializedName("status")
            var status: String,
            @SerializedName("updatedAt")
            var updatedAt: String,
            @SerializedName("__v")
            var v: Int,
            @SerializedName("website")
            var website: Any
    ) {
        data class Location(
                @SerializedName("coordinates")
                var coordinates: List<Double>,
                @SerializedName("type")
                var type: String
        )
    }
}