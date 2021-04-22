package com.youpic.apiConnection.DataModel

import com.google.gson.annotations.SerializedName

data class WinnerAndSelectionDataModel(var status:String, @SerializedName("message")
var message: String)
