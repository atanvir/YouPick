package com.youpic.apiConnection.DataModel


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class HomeDataDataModel(
        @SerializedName("data")
        var `data`: Data?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("status")
        var status: String?
) {
    data class Data(
            @SerializedName("activityArray")
            var activityArray: List<Restaurant>?,
            @SerializedName("incomingCount")
            var incomingCount: String?,
            @SerializedName("incomingData")
            var incomingData: List<IncomingData>,
            @SerializedName("notiCount")
            var notiCount: String?,
            @SerializedName("outgoingCount")
            var outgoingCount: String?,
            @SerializedName("batch")
            var batch: Batch?,
            @SerializedName("outgoingData")
            var outgoingData: List<IncomingData>?,
            @SerializedName("rateStatus")
            var rateStatus: Boolean?,
            @SerializedName("restaurantList")
            var restaurantList: List<Restaurant>?,
            @SerializedName("scheduleCount")
            var scheduleCount: String?,
            @SerializedName("scheduleData")
            var scheduleData: List<ScheduleData>?,
            @SerializedName("userId")
            var userId: String?,
            @SerializedName("userName")
            var userName: String?,
            @SerializedName("country")
            var country: String?,
            @SerializedName("userProfile")
            var userProfile: String?,
            @SerializedName("points")
            var points: String?,
            @SerializedName("pickFavorites")
            var pickFavorites: Boolean?
    ) {

            data class Batch(
                    @SerializedName("createdAt")
                    var createdAt: String,
                    @SerializedName("deleteStatus")
                    var deleteStatus: Boolean,
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("image")
                    var image: String,
                    @SerializedName("maxValue")
                    var maxValue: String,
                    @SerializedName("minValue")
                    var minValue: String,
                    @SerializedName("name")
                    var name: String,
                    @SerializedName("status")
                    var status: String,
                    @SerializedName("updatedAt")
                    var updatedAt: String,
                    @SerializedName("__v")
                    var v: String
            )
        data class ScheduleData(
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
                @SerializedName("distance")
                var distance: String,
                @SerializedName("finalCallDate")
                var finalCallDate: String,
                @SerializedName("finalCallNotification")
                var finalCallNotification: Boolean,
                @SerializedName("finalRestaurantChoice")
                var finalRestaurantChoice: List<FinalRestaurantChoice>,
                @SerializedName("groupCreated")
                var groupCreated: List<Any>,
                @SerializedName("_id")
                var id: String,
                @SerializedName("isTwentyStatus")
                var isTwentyStatus: Boolean,
                @SerializedName("latitude")
                var latitude: String,
                @SerializedName("longitude")
                var longitude: String,
                @SerializedName("meetForType")
                var meetForType: String,
                @SerializedName("meetingDate")
                var meetingDate: String,
                @SerializedName("meetingTime")
                var meetingTime: String,
                @SerializedName("meetingType")
                var meetingType: String,
                @SerializedName("memberData")
                var memberData: List<Any>,
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
            data class FinalRestaurantChoice(
                    @SerializedName("_id")
                    var id: String,
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

            data class UsersData(
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("userId")
                    var userId: String
            )
        }

        data class ActivityArray(
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
                var email: Any?,
                @SerializedName("hours")
                var hours: Any?,
                @SerializedName("_id")
                var id: String?,
                @SerializedName("imageData")
                var imageData: List<Any>?,
                @SerializedName("latitude")
                var latitude: String?,
                @SerializedName("location")
                var location: Location?,
                @SerializedName("longitude")
                var longitude: String?,
                @SerializedName("phoneNumber")
                var phoneNumber: String?,
                @SerializedName("postalCode")
                var postalCode: String?,
                @SerializedName("state")
                var state: String?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("website")
                var website: String?
        ) {
            data class Dist(
                    @SerializedName("calculated")
                    var calculated: String?
            )

            data class Location(
                    @SerializedName("coordinates")
                    var coordinates: List<String>?,
                    @SerializedName("type")
                    var type: String?
            )
        }

        data class IncomingData(
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
                @SerializedName("memberData")
                var memberData: List<MemberData>,
                @SerializedName("__v")
                var v: Int
        ) {
            data class UsersData(
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("userId")
                    var userId: String
            )
            data class MemberData(
                    @SerializedName("cuisineChoice")
                    var cuisineChoice: List<Any>,
                    @SerializedName("deleteStatus")
                    var deleteStatus: Boolean,
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("isAction")
                    var isAction: Boolean,
                    @SerializedName("isFinal")
                    var isFinal: Boolean,
                    @SerializedName("isTimer")
                    var isTimer: Boolean,
                    @SerializedName("meetingId")
                    var meetingId: String,
                    @SerializedName("memberData")
                    var memberData: MemberData?,
                    @SerializedName("restaurantChoice")
                    var restaurantChoice: List<Any>,
                    @SerializedName("status")
                    var status: String,
                    @SerializedName("userId")
                    var userId: String
            ) {
                data class MemberData(
                        @SerializedName("_id")
                        var id: String,
                        @SerializedName("name")
                        var name: String,
                        @SerializedName("profilePic")
                        var profilePic: String?
                )
            }
        }


        @Parcelize
        data class Restaurant (
                @SerializedName("actualId")
                var actualId: String?,
                @SerializedName("address")
                var address: String?,
                @SerializedName("adminImage")
                var adminImage: String?,
                @SerializedName("name")
                var name: String?,
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
                var imageData: ArrayList<ImageData>?= ArrayList(),
                @SerializedName("latitude")
                var latitude: String?,
                @SerializedName("location")
                var location: Location?,
                @SerializedName("longitude")
                var longitude: String?,
                @SerializedName("phoneNumber")
                var phoneNumber: String?,
                @SerializedName("postalCode")
                var postalCode: String?,
                @SerializedName("state")
                var state: String?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("website")
                var website: String?,
                @SerializedName("totalVisited")
                var totalVisited: String?,
                @SerializedName("cuisineData")
                var cuisineData: ArrayList<ImageData>?
        ): Parcelable {
                @Parcelize
                data class ImageData(
                        @SerializedName("createdAt")
                        var createdAt: String?,
                        @SerializedName("_id")
                        var id: String?,
                        @SerializedName("image")
                        var image: String?,
                        @SerializedName("name")
                        var name: String?,
                        @SerializedName("restaurantId")
                        var restaurantId: String?,
                        @SerializedName("status")
                        var status: String?,
                        @SerializedName("updatedAt")
                        var updatedAt: String?,
                        @SerializedName("userId")
                        var userId: String?,
                        @SerializedName("__v")
                        var v: String?
                ): Parcelable
            @Parcelize
            data class Dist(
                    @SerializedName("calculated")
                    var calculated: Double?
            ): Parcelable
            @Parcelize
            data class Location(
                    @SerializedName("coordinates")
                    var coordinates: List<String>?,
                    @SerializedName("type")
                    var type: String?
            ): Parcelable
                @Parcelize
            data class CuisineData(
                    @SerializedName("image")
                    var image: String?

            ): Parcelable

        }
    }
}