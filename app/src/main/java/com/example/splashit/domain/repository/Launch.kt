package com.example.splashit.domain.repository

interface Launch {
    val id: String
    val name: String
    val username: String
    val photo: String
    val profileImage: String
    var likes: Int
    var likedByUser: Boolean
    val height: Int
    val width: Int
}