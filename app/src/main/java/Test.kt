
import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Int
) {
    data class Data(
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("_id")
        var id: String,
        @SerializedName("isSeen")
        var isSeen: Boolean,
        @SerializedName("notiMessage")
        var notiMessage: String,
        @SerializedName("notiTitle")
        var notiTitle: String,
        @SerializedName("notiTo")
        var notiTo: String,
        @SerializedName("notiType")
        var notiType: String,
        @SerializedName("status")
        var status: String,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("__v")
        var v: Int
    )
}