package com.example.splashit.domain.models

data class CollectionPhotos(
    val id: String,
    val userName: String,
    val userNick: String,
    val photo: String,
    val profilePhoto: String,
    var totalLikes: Int,
    var likedByUser: Boolean,
    var totalDownloads: Int? = null
)