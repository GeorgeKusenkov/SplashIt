package com.example.splashit.domain.models

import com.example.splashit.data.models.currentuser.ProfileImage

class CurrentUserInfo(
    val id: String,
    val first_name: String,
    val last_name: String,
    val username: String,
    val bio: String,
    val location: String,
    val email: String,
    val downloads: Int,
    val profileImage: ProfileImage,
    val totalCollections: Int,
    val totalLikes: Int
)