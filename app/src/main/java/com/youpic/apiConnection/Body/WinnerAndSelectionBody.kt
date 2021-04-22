package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class WinnerAndSelectionBody(
    @SerializedName("meetingId")
    var meetingId: String,
    @SerializedName("restaurantAddress")
    var restaurantAddress: String,
    @SerializedName("restaurantId")
    var restaurantId: String,
    @SerializedName("restaurantLatitude")
    var restaurantLatitude: String,
    @SerializedName("restaurantLongitude")
    var restaurantLongitude: String,
    @SerializedName("restaurantName")
    var restaurantName: String
)