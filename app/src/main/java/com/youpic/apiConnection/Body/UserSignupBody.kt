package com.youpic.apiConnection.Body

import com.google.gson.annotations.SerializedName

class UserSignupBody
{

    @SerializedName("name")
    lateinit var name:String

    @SerializedName("countryCode")
    lateinit var countryCode:String

    @SerializedName("mobileNumber")
    lateinit var mobileNumber:String

    @SerializedName("latitude")
    lateinit var latitude:String

    @SerializedName("longitude")
    lateinit var longitude:String

    @SerializedName("timezone")
    lateinit var timezone:String

    @SerializedName("deviceType")
    lateinit var deviceType:String

    @SerializedName("deviceToken")
    lateinit var deviceToken:String

    @SerializedName("password")
    lateinit var password:String

    @SerializedName("country")
    lateinit var country:String

    @SerializedName("pickFavorites")
    lateinit var pickFavorites:ArrayList<String>

}