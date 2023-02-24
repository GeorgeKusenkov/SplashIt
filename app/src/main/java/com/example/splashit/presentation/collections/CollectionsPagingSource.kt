package com.example.splashit.presentation.collections

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splashit.data.repository.CollectionsListRepositoryImpl
import com.example.splashit.domain.models.CollectionsList

class CollectionsPagingSource: PagingSource<Int, CollectionsList>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionsList>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionsList> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            CollectionsListRepositoryImpl().getCollections(page = page)
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