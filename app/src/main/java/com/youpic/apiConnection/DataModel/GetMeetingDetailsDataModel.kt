package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName


data class GetMeetingDetailsDataModel(
        @SerializedName("data")
        var `data`: Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("groupCreatedStatus")
            var groupCreatedStatus: Boolean,
            @SerializedName("meetingData")
            var meetingData: MeetingData,
            @SerializedName("meetingUsersData")
            var meetingUsersData: List<MeetingUsersData>
    ) {
        data class MeetingData(
                @SerializedName("actualProcessStatus")
                var actualProcessStatus: Boolean?,
                @SerializedName("cancelRequestBy")
                var cancelRequestBy: List<Any>?,
                @SerializedName("createdAt")
                var createdAt: String?,
                @SerializedName("cuisineChoice")
                var cuisineChoice: List<Any>?,
                @SerializedName("deleteStatus")
                var deleteStatus: Boolean?,
                @SerializedName("distance")
                var distance: String?,
                @SerializedName("finalCallDate")
                var finalCallDate: String?,
                @SerializedName("finalCallNotification")
                var finalCallNotification: Boolean?,
                @SerializedName("finalRestaurantChoice")
                var finalRestaurantChoice: List<FinalRestaurantChoice>?,
                @SerializedName("groupCreated")
                var groupCreated: List<Any>?,
                @SerializedName("_id")
                var id: String?,
                @SerializedName("isTwentyStatus")
                var isTwentyStatus: Boolean?,
                @SerializedName("latitude")
                var latitude: String?,
                @SerializedName("longitude")
                var longitude: String?,
                @SerializedName("meetForType")
                var meetForType: String?,
                @SerializedName("meetingDate")
                var meetingDate: String?,
                @SerializedName("meetingTime")
                var meetingTime: String?,
                @SerializedName("restaurantName")
                var restaurantName: String?,
                @SerializedName("restaurantAddress")
                var restaurantAddress: String?,
                @SerializedName("restaurantLatitude")
                var restaurantLatitude: String?,
                @SerializedName("restaurantLongitude")
                var restaurantLongitude: String?,
                @SerializedName("meetingType")
                var meetingType: String?,
                @SerializedName("ratingBy")
                var ratingBy: List<Any>?,
                @SerializedName("restaurantChoice")
                var restaurantChoice: List<Any>?,
                @SerializedName("restaurantFor")
                var restaurantFor: String?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("takePictureStatus")
                var takePictureStatus: Boolean?,
                @SerializedName("timezone")
                var timezone: String?,
                @SerializedName("type")
                var type: String?,
                @SerializedName("updatedAt")
                var updatedAt: String?,
                @SerializedName("userId")
                var userId: String?,
                @SerializedName("usersData")
                var usersData: List<UsersData>?,
                @SerializedName("__v")
                var v: Int?
        ) {
            data class FinalRestaurantChoice(
                    @SerializedName("_id")
                    var id: String?,
                    @SerializedName("restaurantAddress")
                    var restaurantAddress: String?,
                    @SerializedName("restaurantId")
                    var restaurantId: String?,
                    @SerializedName("restaurantLatitude")
                    var restaurantLatitude: String?,
                    @SerializedName("restaurantLongitude")
                    var restaurantLongitude: String?,
                    @SerializedName("restaurantName")
                    var restaurantName: String?,
                    @SerializedName("userId")
                    var userId: String?
            )

            data class UsersData(
                    @SerializedName("_id")
                    var id: String?,
                    @SerializedName("userId")
                    var userId: String?
            )
        }

        data class MeetingUsersData(
                @SerializedName("createdAt")
                var createdAt: String?,
                @SerializedName("cuisineChoice")
                var cuisineChoice: List<String>?,
                @SerializedName("deleteStatus")
                var deleteStatus: Boolean?,
                @SerializedName("_id")
                var id: String?,
                @SerializedName("isAction")
                var isAction: Boolean?,
                @SerializedName("isFinal")
                var isFinal: Boolean?,
                @SerializedName("isTimer")
                var isTimer: Boolean?,
                @SerializedName("meetingId")
                var meetingId: String?,
                @SerializedName("restaurantChoice")
                var restaurantChoice: List<RestaurantChoice>?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("updatedAt")
                var updatedAt: String?,
                @SerializedName("userId")
                var userId: UserId?,
                @SerializedName("__v")
                var v: Int?
        ) {
            data class RestaurantChoice(
                    @SerializedName("_id")
                    var id: String?,
                    @SerializedName("restaurantAddress")
                    var restaurantAddress: String?,
                    @SerializedName("restaurantId")
                    var restaurantId: String?,
                    @SerializedName("restaurantLatitude")
                    var restaurantLatitude: String?,
                    @SerializedName("restaurantLongitude")
                    var restaurantLongitude: String?,
                    @SerializedName("restaurantName")
                    var restaurantName: String?,
                    @SerializedName("userId")
                    var userId: String?
            )

            data class UserId(
                    @SerializedName("_id")
                    var id: String?,
                    @SerializedName("name")
                    var name: String?,
                    @SerializedName("profilePic")
                    var profilePic: String?
            )
        }
    }
}