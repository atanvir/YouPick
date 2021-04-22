package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class DirectMeetingRequestBody(
    @SerializedName("meetForType")
    var meetForType: String,
    @SerializedName("meetingDate")
    var meetingDate: String?,
    @SerializedName("meetingTime")
    var meetingTime: String?,
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
    @SerializedName("type")
    var type: String,
    @SerializedName("usersData")
    var usersData: List<MeetingRequestBody.UsersData>
) {
    data class UsersData(
        @SerializedName("userId")
        var userId: String
    )
}