package com.example.splashit.domain.models

data class UsersPhoto (
    val id: String,
    val name: String,
    val username: String,
    val photo: String,
    val profileImage: String,
    var likes: Int,
    var likedByUser: Boolean
)