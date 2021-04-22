package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class UpdatePhoneConatctBody(
    @SerializedName("phoneContact")
    var phoneContact: List<PhoneContact>?
) {
    data class PhoneContact(
        @SerializedName("countryCode")
        var countryCode: String,
        @SerializedName("mobileNumber")
        var mobileNumber: String,
        @SerializedName("name")
        var name: String
    )
}