package com.example.splashit.domain.repository

import com.example.splashit.data.models.currentuser.CurrentUserDto
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.domain.models.CollectionsList
import com.example.splashit.domain.models.CurrentUserInfo
import com.example.splashit.domain.models.CurrentUserLikedPhotos

interface CurrentUserRepository {
    suspend fun getCurrentUserData(): CurrentUserInfo
    suspend fun getCurrentUserLikedPhotos(page: Int): List<CurrentUserLikedPhotos>
    suspend fun getCurrentUserPhoto(page: Int): List<CollectionPhotos>
    suspend fun getCurrentUserCollections(page: Int): List<CollectionsList>
    suspend fun likePhoto(id: String): CurrentUserDto
    suspend fun unlikePhoto(id: String): CurrentUserDto

}