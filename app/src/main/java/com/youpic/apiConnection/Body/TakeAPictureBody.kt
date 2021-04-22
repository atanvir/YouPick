package com.youpic.apiConnection.Body

import com.google.gson.annotations.SerializedName

data class TakeAPictureBody(

        @SerializedName("notificationId")
        var notificationId:String,
        @SerializedName("restaurantId")
        var restaurantId:String
)
