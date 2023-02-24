package com.example.splashit.data.room

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.splashit.data.api.Api
import com.example.splashit.data.room.models.RoomUsersPhoto

@ExperimentalPagingApi
class LaunchRemoteMediator(
    private val launchesDao: UsersPhotoDao,
    private val launchesApi: Api
) : RemoteMediator<Int, RoomUsersPhoto>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RoomUsersPhoto>
    ): MediatorResult {
        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize

        try {
            val a = launchesApi.getListUsersPhoto(pageIndex)
            val photos = a.map {
                RoomUsersPhoto(
                    id = it.id,
                    name = it.user.name,
                    username = it.user.username,
                    photo = it.urls.small,
                    profileImage = it.user.profile_image.small,
                    likes = it.likes,
                    likedByUser = it.liked_by_user,
                    height = it.height,
                    width = it.width
                )
            }
            if(loadType == LoadType.REFRESH) {
                launchesDao.refresh(photos)
            } else {
                launchesDao.save(photos)
            }
            return MediatorResult.Success(endOfPaginationReached = photos.size < limit)

        }
        catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }
}