package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckUserMobileNumberDataModel2
{
    @SerializedName("status")
    @Expose
    var status:String?=null
    @SerializedName("message")
    @Expose
    var message:String?=null
    @SerializedName("data")
    @Expose
    var data:Data?=null

    inner class Data{
        @SerializedName("otp")
        @Expose
        lateinit var otp:String
    }
}