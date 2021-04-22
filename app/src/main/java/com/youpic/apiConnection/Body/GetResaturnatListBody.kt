package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class GetResaturnatListBody(
    @SerializedName("pageNumber")
    var pageNumber: String,
    @SerializedName("meetingId")
    var meetingId: String,
    @SerializedName("count")
    var count: String,
    @SerializedName("cuisineData")
    var cuisineData: ArrayList<String>,
    /*@SerializedName("distance")
    var distance: String,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("longitude")
    var longitude: String,*/
    @SerializedName("requestUserData")
    var requestUserData: ArrayList<String>
)