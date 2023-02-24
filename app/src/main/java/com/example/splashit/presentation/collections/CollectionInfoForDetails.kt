package com.example.splashit.presentation.collections

import com.example.splashit.data.models.collections.Tag

data class CollectionInfoForDetails(
    val id: String,
    val title: String,
    val tags: List<Tag>,
    val description: String,
    val author: String,
    val totalPhotos: Int,
    val coverPhoto: String
)