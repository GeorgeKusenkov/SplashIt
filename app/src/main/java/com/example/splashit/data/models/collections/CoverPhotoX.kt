package com.example.splashit.data.models.collections

data class CoverPhotoX(
    val alt_description: String,
    val blur_hash: String,
    val color: String,
    val created_at: String,
    val current_user_collections: List<Any>,
    val description: String,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val premium: Boolean,
    val promoted_at: String,
    val sponsorship: Any,
    val updated_at: String,
    val urls: Urls,
    val user: UserX,
    val width: Int
)