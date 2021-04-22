package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetMeetingInviteDetailsDataModel(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
) {
    data class Data(
        @SerializedName("meetingByData")
        var meetingByData: MeetingByData,
        @SerializedName("meetingData")
        var meetingData: MeetingData,
        @SerializedName("memberList")
        var memberList: List<Member>
    ) {
        data class MeetingByData(
            @SerializedName("_id")
            var id: String,
            @SerializedName("name")
            var name: String,
            @SerializedName("profilePic")
            var profilePic: String
        )

        data class MeetingData(
            @SerializedName("actualProcessStatus")
            var actualProcessStatus: Boolean,
            @SerializedName("cancelRequestBy")
            var cancelRequestBy: List<Any>,
            @SerializedName("createdAt")
            var createdAt: String,
            @SerializedName("cuisineChoice")
            var cuisineChoice: List<Any>,
            @SerializedName("deleteStatus")
            var deleteStatus: Boolean,
            @SerializedName("finalCallNotification")
            var finalCallNotification: Boolean,
            @SerializedName("finalRestaurantChoice")
            var finalRestaurantChoice: List<Any>,
            @SerializedName("groupCreated")
            var groupCreated: List<Any>,
            @SerializedName("_id")
            var id: String,
            @SerializedName("isTwentyStatus")
            var isTwentyStatus: Boolean,
            @SerializedName("meetForType")
            var meetForType: String,
            @SerializedName("meetingDate")
            var meetingDate: String,
            @SerializedName("meetingTime")
            var meetingTime: String,
            @SerializedName("latitude")
            var latitude: String?,
            @SerializedName("longitude")
            var longitude: String?,
            @SerializedName("distance")
            var distance: String?,
            @SerializedName("meetingType")
            var meetingType: String,
            @SerializedName("ratingBy")
            var ratingBy: List<Any>,
            @SerializedName("restaurantAddress")
            var restaurantAddress: String,
            @SerializedName("restaurantChoice")
            var restaurantChoice: List<Any>,
            @SerializedName("restaurantFor")
            var restaurantFor: String,
            @SerializedName("restaurantId")
            var restaurantId: String,
            @SerializedName("restaurantLatitude")
            var restaurantLatitude: String,
            @SerializedName("restaurantLongitude")
            var restaurantLongitude: String,
            @SerializedName("restaurantName")
            var restaurantName: String,
            @SerializedName("status")
            var status: String,
            @SerializedName("takePictureStatus")
            var takePictureStatus: Boolean,
            @SerializedName("timezone")
            var timezone: String,
            @SerializedName("type")
            var type: String,
            @SerializedName("updatedAt")
            var updatedAt: String,
            @SerializedName("userId")
            var userId: String,
            @SerializedName("usersData")
            var usersData: List<UsersData>,
            @SerializedName("__v")
            var v: Int
        ) {
            data class UsersData(
                @SerializedName("_id")
                var id: String,
                @SerializedName("userId")
                var userId: String
            )
        }

        data class Member(
            @SerializedName("_id")
            var id: String,
            @SerializedName("isNotificationSend")
            var isNotificationSend: Boolean,
            @SerializedName("memberData")
            var memberData: MemberData
        ) {
            data class MemberData(
                @SerializedName("_id")
                var id: String,
                @SerializedName("name")
                var name: String,
                @SerializedName("profilePic")
                var profilePic: String
            )
        }
    }
}