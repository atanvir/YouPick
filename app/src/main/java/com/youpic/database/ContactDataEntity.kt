package com.youpic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/*@SerializedName("countryCode")
        var countryCode: String,
        @SerializedName("mobileNumber")
        var mobileNumber: String,
        @SerializedName("name")
        var name: String*/

@Entity(tableName = "ContactListTable")
data class ContactDataEntity(@PrimaryKey val id:Int,
                             @ColumnInfo(name="countryCode") val countryCode:String,
                             @ColumnInfo(name="mobileNumber") val mobileNumber:String,
                             @ColumnInfo(name="name") val name:String)
{

}