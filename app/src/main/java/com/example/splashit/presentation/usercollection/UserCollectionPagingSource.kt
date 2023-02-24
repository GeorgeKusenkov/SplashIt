package com.example.splashit.presentation.usercollection

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splashit.data.repository.CollectionsListRepositoryImpl
import com.example.splashit.domain.models.CollectionPhotos

class UserCollectionPagingSource(val id: String): PagingSource<Int, CollectionPhotos>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionPhotos>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionPhotos> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            CollectionsListRepositoryImpl().getCollectionPhotos(id = id, page = page)
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