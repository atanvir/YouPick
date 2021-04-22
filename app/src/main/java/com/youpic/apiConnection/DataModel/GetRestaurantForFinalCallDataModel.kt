package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName


data class GetRestaurantForFinalCallDataModel(
        @SerializedName("data")
        var `data`: Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("finalRestaurant")
            var finalRestaurant: ArrayList<FinalRestaurant>,
            @SerializedName("memberRestaurant")
            var memberRestaurant: List<MemberRestaurant>
    ) {
        data class FinalRestaurant(
                @SerializedName("restaurant")
                var restaurant: Restaurant,
                @SerializedName("user")
                var user: User,
                var userList: ArrayList<User> = ArrayList<User>()
        ) {
            data class Restaurant(
                    @SerializedName("address")
                    var address: String,
                    @SerializedName("adminImage")
                    var adminImage: String,
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("cuisineArray")
                    var cuisineArray: ArrayList<String>,
                    @SerializedName("latitude")
                    var latitude: String,
                    @SerializedName("longitude")
                    var longitude: String,
                    @SerializedName("name")
                    var name: String
            )

            data class User(
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("name")
                    var name: String,
                    @SerializedName("profilePic")
                    var profilePic: String
            )
        }

        data class MemberRestaurant(
                @SerializedName("cuisineChoice")
                var cuisineChoice: List<String>,
                @SerializedName("_id")
                var id: String,
                @SerializedName("restaurantChoice")
                var restaurantChoice: List<RestaurantChoice>,
                @SerializedName("status")
                var status: String
        ) {
            data class RestaurantChoice(
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
        }
    }
}