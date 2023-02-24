package com.example.splashit.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.splashit.data.room.models.RoomUsersPhoto

@Dao
interface UsersPhotoDao {
    @Query("SELECT * FROM users_photo")
    fun getAll(): PagingSource<Int, RoomUsersPhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(photo: List<RoomUsersPhoto>)

    @Query("DELETE FROM users_photo")
    suspend fun clear()

    @Transaction
    suspend fun refresh(photo: List<RoomUsersPhoto>) {
        clear()
        save(photo)
    }
}
