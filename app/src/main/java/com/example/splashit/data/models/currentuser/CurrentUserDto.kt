package com.example.splashit.data.models.currentuser

data class CurrentUserDto(
    val bio: String,
    val downloads: Int,
    val email: String,
    val first_name: String,
    val followed_by_user: Boolean,
    val id: String,
    val profile_image: ProfileImage,
    val instagram_username: String,
    val last_name: String,
    val links: Links,
    val location: String,
    val portfolio_url: Any,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val twitter_username: String,
    val updated_at: String,
    val uploads_remaining: Int,
    val username: String
)