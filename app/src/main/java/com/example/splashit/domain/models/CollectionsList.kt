package com.example.splashit.domain.models

import com.example.splashit.data.models.collections.Tag

data class CollectionsList(
    val id: String,
    val totalPhotos: Int,
    val collectionsName: String,
    val coverPhoto: String,
    val user: String,
    val username: String,
    val profileImage: String,
    val description: String,
    val tags: List<Tag>
)