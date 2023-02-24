package com.example.splashit.data.repository

import com.example.splashit.data.api.Api
import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.domain.models.CurrentUserPhotos
import com.example.splashit.domain.models.UsersPhoto
import com.example.splashit.domain.repository.UsersPhotoRepository

class UnsplashPhotoRepositoryImpl : UsersPhotoRepository {
    override suspend fun getListUnsplashPhoto(page: Int): List<UsersPhoto> {
        val apiUsersPhoto = Api.retrofit.getListUsersPhoto(page)
        val usersPhoto = mutableListOf<UsersPhoto>()
        apiUsersPhoto.forEach {
            usersPhoto.add(
                UsersPhoto(
                    id = it.id,
                    name = it.user.name,
                    username = it.user.username,
                    photo = it.urls.small,
                    profileImage = it.user.profile_image.medium,
                    likes = it.likes,
                    likedByUser = it.liked_by_user
                )
            )
        }
        return usersPhoto
    }

    override suspend fun getCurrentUserPhotos(page: Int): List<CurrentUserPhotos> {
        val userPhotos = Api.retrofit.getUserPhotos("g_kussenkov",page)
        val usersPhoto = mutableListOf<CurrentUserPhotos>()

        userPhotos.forEach {
            usersPhoto.add(
                CurrentUserPhotos(
                    id = it.id,
                    name = it.user.name,
                    username = it.user.username,
                    photo = it.urls.small,
                    profileImage = it.user.profile_image.medium,
                    likes = it.likes,
                    likedByUser = it.liked_by_user
                )
            )
        }
        return usersPhoto
    }

    override suspend fun getUnsplashPhotoDetails(id: String): DetailUnsplashPhoto {
        return Api.retrofit.getUnsplashPhotoDetails(id = id)
    }
}