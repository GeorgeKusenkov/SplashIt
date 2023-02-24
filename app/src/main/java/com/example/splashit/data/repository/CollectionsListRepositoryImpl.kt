package com.example.splashit.data.repository

import com.example.splashit.data.api.Api
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.domain.models.CollectionsList
import com.example.splashit.domain.repository.CollectionsListRepository

class CollectionsListRepositoryImpl: CollectionsListRepository {
    override suspend fun getCollections(page: Int): List<CollectionsList> {
        val apiUsersPhoto = Api.retrofit.getListOfCollections(page = page)
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

    override suspend fun getCollectionPhotos(id: String, page: Int): List<CollectionPhotos> {
        val apiCollectionPhotos = Api.retrofit.getCollectionPhotos(id = id, page = page)
        val listOfPhoto = mutableListOf<CollectionPhotos>()
        apiCollectionPhotos.forEach {
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
}