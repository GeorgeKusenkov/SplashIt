package com.example.splashit.data.models.userinfo

data class RelatedCollections(
    val results: List<Result>,
    val total: Int,
    val type: String
)