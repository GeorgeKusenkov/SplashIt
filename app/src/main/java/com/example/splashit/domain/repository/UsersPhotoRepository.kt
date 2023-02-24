package com.example.splashit.domain.repository

import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.domain.models.CurrentUserPhotos
import com.example.splashit.domain.models.UsersPhoto

interface UsersPhotoRepository {
    suspend fun getListUnsplashPhoto(page: Int): List<UsersPhoto>
    suspend fun getCurrentUserPhotos(page: Int): List<CurrentUserPhotos>
    suspend fun getUnsplashPhotoDetails(id: String): DetailUnsplashPhoto
}