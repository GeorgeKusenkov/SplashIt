package com.example.splashit.presentation.curentuser.likedphotos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splashit.data.repository.CurrentUserRepositoryImpl
import com.example.splashit.domain.models.CurrentUserLikedPhotos

class LikedUserPhotosPagingSource: PagingSource<Int, CurrentUserLikedPhotos>() {
    override fun getRefreshKey(state: PagingState<Int, CurrentUserLikedPhotos>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CurrentUserLikedPhotos> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            CurrentUserRepositoryImpl().getCurrentUserLikedPhotos(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )

            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }
}