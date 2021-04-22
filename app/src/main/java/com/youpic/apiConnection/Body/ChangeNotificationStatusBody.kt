package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class ChangeNotificationStatusBody(
    @SerializedName("notificationStatus")
    var notificationStatus: Boolean,
    @SerializedName("soundStatus")
    var soundStatus: Boolean
)