package com.example.splashit.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.splashit.domain.repository.Launch

@Entity(tableName = "users_photo")
data class RoomUsersPhoto(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "username")
    override val username: String,

    @ColumnInfo(name = "photo")
    override val photo: String,

    @ColumnInfo(name = "profileImage")
    override val profileImage: String,

    @ColumnInfo(name = "likes")
    override var likes: Int,

    @ColumnInfo(name = "likedByUser")
    override var likedByUser: Boolean,

    @ColumnInfo(name = "height")
    override val height: Int,

    @ColumnInfo(name = "width")
    override val width: Int
) : Launch {
}