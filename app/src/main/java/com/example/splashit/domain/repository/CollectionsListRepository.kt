package com.example.splashit.domain.repository

import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.domain.models.CollectionsList

interface CollectionsListRepository {
    suspend fun getCollections(page: Int): List<CollectionsList>
    suspend fun getCollectionPhotos(id:String, page: Int): List<CollectionPhotos>
}