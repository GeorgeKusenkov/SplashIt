package com.example.splashit.data.repository

import com.example.splashit.data.api.Api
import com.example.splashit.data.models.currentuser.CurrentUserDto
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.domain.models.CollectionsList
import com.example.splashit.domain.models.CurrentUserInfo
import com.example.splashit.domain.models.CurrentUserLikedPhotos
import com.example.splashit.domain.repository.CurrentUserRepository

class CurrentUserRepositoryImpl : CurrentUserRepository {
    private var currentUserName = ""
    override suspend fun getCurrentUserData(): CurrentUserInfo {
        currentUserName = Api.retrofit.getCurrentUserInfo().username
        val user = Api.retrofit.getCurrentUserInfo()
        currentUserName = user.username
        return CurrentUserInfo(
            id = user.id,
            first_name = user.first_name,
            last_name = user.last_name,
            username = user.username,
            bio = user.bio,
            location = user.location,
            email = user.email,
            downloads = user.downloads,
            profileImage = user.profile_image,
            totalCollections = user.total_collections,
            totalLikes = user.total_likes
        )
    }

    override suspend fun getCurrentUserLikedPhotos(page: Int): List<CurrentUserLikedPhotos> {
        currentUserName = Api.retrofit.getCurrentUserInfo().username
        val likedPhotos = Api.retrofit.getCurrentUserLikedPhotos(currentUserName, page)
        val listOfPhoto = mutableListOf<CurrentUserLikedPhotos>()
        likedPhotos.forEach {
            listOfPhoto.add(
                CurrentUserLikedPhotos(
                    id = it.id,
                    photo = it.urls.regular
                )
            )
        }
        return listOfPhoto
    }

    override suspend fun getCurrentUserPhoto(page: Int): List<CollectionPhotos> {
        currentUserName = Api.retrofit.getCurrentUserInfo().username
        val userPhotos = Api.retrofit.getUserPhotos(currentUserName, page)
        val listOfPhoto = mutableListOf<CollectionPhotos>()
        userPhotos.forEach {
            listOfPhoto.add(
                CollectionPhotos(
                    id = it.id,
                    userName = it.user.name,
                    userNick = it.user.username,
                    photo = it.urls.regular,
                    profilePhoto = it.user.profile_image.small,
                    totalLikes = it.likes,
                    likedByUser = it.liked_by_user,
                    totalDownloads = Api.retrofit.getUnsplashPhotoDetails(id = it.id).downloads
                )
            )
        }
        return listOfPhoto
    }

    override suspend fun getCurrentUserCollections(page: Int): List<CollectionsList> {
        currentUserName = Api.retrofit.getCurrentUserInfo().username
        val apiUsersPhoto = Api.retrofit.getCurrentUserListCollections(currentUserName, page)
        val listOfCollections = mutableListOf<CollectionsList>()
        apiUsersPhoto.forEach {
            listOfCollections.add(
                CollectionsList(
                    id = it.id,
                    totalPhotos = it.total_photos,
                    collectionsName = it.title,
                    coverPhoto = it.cover_photo.urls.regular,
                    user = it.user.name,
                    username = it.user.username,
                    profileImage = it.user.profile_image.small,
                    description = it.cover_photo.alt_description,
                    tags = it.tags
                )
            )
        }
        return listOfCollections
    }

    override suspend fun likePhoto(id: String): CurrentUserDto {
        return Api.retrofit.likePhoto(id)
    }

    override suspend fun unlikePhoto(id: String): CurrentUserDto {
        return Api.retrofit.unlikePhoto(id)
    }
}