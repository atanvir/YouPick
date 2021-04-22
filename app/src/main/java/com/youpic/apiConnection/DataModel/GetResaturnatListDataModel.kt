package com.youpic.apiConnection.DataModel


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetResaturnatListDataModel(
    @SerializedName("data")
    var `data`: CuisineRestuarants ?,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
):Parcelable {
    @Parcelize
    data class CuisineRestuarants(

         @SerializedName("cuisine1Restuarants")
         var cuisine1Restuarants: ArrayList<Data>?,

         @SerializedName("cuisine2Restuarants")
         var cuisine2Restuarants: ArrayList<Data>?,

         @SerializedName("cuisine3Restuarants")
         var cuisine3Restuarants: ArrayList<Data>?
         ):Parcelable

    @Parcelize
    data class Data(
            @SerializedName("actualId")
            var actualId: String?,
            @SerializedName("address")
            var address: String?,
            @SerializedName("adminImage")
            var adminImage: String?,
            @SerializedName("city")
            var city: String?,
            @SerializedName("cuisineArray")
            var cuisineArray: List<String>?,
            @SerializedName("dist")
            var dist: Dist?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("hours")
            var hours: String?,
            @SerializedName("_id")
            var id: String?,
            @SerializedName("imageData")
            var imageData: ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?=ArrayList(),
            @SerializedName("latitude")
            var latitude: String?,
            @SerializedName("location")
            var location: Location?,
            @SerializedName("longitude")
            var longitude: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("phoneNumber")
            var phoneNumber: String?,
            @SerializedName("postalCode")
            var postalCode: String?,
            @SerializedName("state")
            var state: String?,
            @SerializedName("status")
            var status: String?,
            @SerializedName("totalVisited")
            var totalVisited: Int?,
            @SerializedName("userData")
            var userData: List<UserData>?,
            @SerializedName("website")
            var website: String?,
            @SerializedName("cuisineData")
            var cuisineData: ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?
    ):Parcelable {
        @Parcelize
        data class Dist(
                @SerializedName("calculated")
                var calculated: Double?
        ):Parcelable

        @Parcelize
        data class Location(
                @SerializedName("coordinates")
                var coordinates: List<Double>,
                @SerializedName("type")
                var type: String
        ):Parcelable
        @Parcelize
        data class UserData(
                @SerializedName("distance")
                var distance: Double,
                @SerializedName("latitude")
                var latitude: String,
                @SerializedName("longitude")
                var longitude: String,
                @SerializedName("name")
                var name: String,
                @SerializedName("profilePic")
                var profilePic: String,
                @SerializedName("userId")
                var userId: String
        ):Parcelable
        @Parcelize
        data class CuisineData(
                @SerializedName("image")
                var image: String?

        ): Parcelable
    }





}