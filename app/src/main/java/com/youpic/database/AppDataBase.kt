package com.youpic.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactDataEntity::class],version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getContactDao():ContactDao
}