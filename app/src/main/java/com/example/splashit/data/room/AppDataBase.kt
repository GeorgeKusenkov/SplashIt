package com.example.splashit.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.splashit.data.room.models.RoomUsersPhoto

@Database(entities = [RoomUsersPhoto::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun usersPhotoDao(): UsersPhotoDao
}