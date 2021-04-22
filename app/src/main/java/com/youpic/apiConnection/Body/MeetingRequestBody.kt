package com.youpic.apiConnection.Body


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MeetingRequestBody(
    @SerializedName("cuisineChoice")
    var cuisineChoice: List<String>?,
    @SerializedName("distance")
    var distance: String,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("meetForType")
    var meetForType: String,
    @SerializedName("meetingDate")
    var meetingDate: String?,
    @SerializedName("meetingTime")
    var meetingTime: String?,
    @SerializedName("restaurantChoice")
    var restaurantChoice: List<RestaurantChoice>?,
    @SerializedName("restaurantFor")
    var restaurantFor: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("usersData")
    var usersData: List<UsersData>
) {
    data class RestaurantChoice(
        @SerializedName("restaurantAddress")
        var restaurantAddress: String,
        @SerializedName("restaurantId")
        var restaurantId: String,
        @SerializedName("restaurantLatitude")
        var restaurantLatitude: Double,
        @SerializedName("restaurantLongitude")
        var restaurantLongitude: Double,
        @SerializedName("restaurantName")
        var restaurantName: String,
        @SerializedName("userId")
        var userId: String
    )

    @Parcelize
    data class UsersData(
        @SerializedName("userId")
        var userId: String,
        @Transient
        var name:String ,
        @Transient
        var lat:String,
        @Transient
        var _long:String
    ):Parcelable
}