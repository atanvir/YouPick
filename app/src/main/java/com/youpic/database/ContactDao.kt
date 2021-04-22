package com.youpic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("select * from ContactListTable")
    fun getAllContact():List<ContactDataEntity>


}