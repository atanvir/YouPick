package com.youpic.apiConnection.DataModel


import com.google.gson.annotations.SerializedName

data class GetFrndGroupListDataModel(
        @SerializedName("data")
        var `data`: ArrayList<Data>,
        @SerializedName("message")
        var message: String,
        @SerializedName("status")
        var status: String
) {
    data class Data(
            @SerializedName("createdAt")
            var createdAt: String,
            @SerializedName("deleteStatus")
            var deleteStatus: Boolean,
            @SerializedName("_id")
            var id: String,
            @SerializedName("memberData")
            var memberData: List<MemberData>,
            @SerializedName("status")
            var status: String,
            @SerializedName("updatedAt")
            var updatedAt: String,
            @SerializedName("userId")
            var userId: String,
            @SerializedName("__v")
            var v: Int
    ) {
        data class MemberData(
                @SerializedName("_id")
                var id: String,
                @SerializedName("memberData")
                var memberData: MemberData
        ) {
            data class MemberData(
                    @SerializedName("_id")
                    var id: String,
                    @SerializedName("name")
                    var name: String,
                    @SerializedName("profilePic")
                    var profilePic: String,
                    @SerializedName("latitude")
                    var lat: String,
                    @SerializedName("longitude")
                    var _long: String

            )
        }
    }
}