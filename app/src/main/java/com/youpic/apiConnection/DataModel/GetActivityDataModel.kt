package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName



data class GetActivityDataModel(
        @SerializedName("data")
        var `data`: Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("activityArray")
            var restaurantList: List<HomeDataDataModel.Data.Restaurant>
    )}