package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class MeetingRequestProcessedBody(
    @SerializedName("cuisineChoice")
    var cuisineChoice: List<Any>?,
    @SerializedName("meetingId")
    var meetingId: String,
    @SerializedName("restaurantChoice")
    var restaurantChoice: List<RestaurantChoice>?
)data class RestaurantChoice(
        @SerializedName("restaurantAddress")
        var restaurantAddress: String,
        @SerializedName("restaurantId")
        var restaurantId: String,
        @SerializedName("restaurantLatitude")
        var restaurantLatitude: String,
        @SerializedName("restaurantLongitude")
        var restaurantLongitude: String,
        @SerializedName("restaurantName")
        var restaurantName: String,
        @SerializedName("userId")
        var userId: String
)