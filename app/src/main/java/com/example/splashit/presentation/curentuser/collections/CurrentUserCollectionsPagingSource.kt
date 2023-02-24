package com.example.splashit.presentation.curentuser.collections

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splashit.data.repository.CurrentUserRepositoryImpl
import com.example.splashit.domain.models.CollectionsList

class CurrentUserCollectionsPagingSource: PagingSource<Int, CollectionsList>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionsList>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionsList> {
        val page = params.key ?: 1
        return kotlin.runCatching {
            CurrentUserRepositoryImpl().getCurrentUserCollections(page)
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