package com.example.splashit.presentation.curentuser.photo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splashit.data.repository.CurrentUserRepositoryImpl
import com.example.splashit.domain.models.CollectionPhotos

class CurrentUserPhotoPagingSource: PagingSource<Int, CollectionPhotos>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionPhotos>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionPhotos> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            CurrentUserRepositoryImpl().getCurrentUserPhoto(page)
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