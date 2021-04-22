package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName

data class DirectAcceptMeetingRequestDataModel(
        @SerializedName("data")
        var `data`: GetMeetingInviteDetailsDataModel.Data,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
)