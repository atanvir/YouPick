package com.youpic.apiConnection.Body


import com.google.gson.annotations.SerializedName

data class AddFavoritePickBody(
    @SerializedName("pickFavorites")
    var pickFavorites: List<String>
)