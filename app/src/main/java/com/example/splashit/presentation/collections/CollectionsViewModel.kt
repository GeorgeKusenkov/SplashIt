package com.example.splashit.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splashit.domain.models.CollectionsList
import com.example.splashit.presentation.curentuser.collections.CurrentUserCollectionsPagingSource
import kotlinx.coroutines.flow.Flow

class CollectionsViewModel: ViewModel() {
    val collections: Flow<PagingData<CollectionsList>> = Pager (
        config = PagingConfig(pageSize = 1),
        pagingSourceFactory = { CollectionsPagingSource() }
    ).flow.cachedIn(viewModelScope)

    val currentUserCollections: Flow<PagingData<CollectionsList>> = Pager (
        config = PagingConfig(pageSize = 1),
        pagingSourceFactory = { CurrentUserCollectionsPagingSource() }
    ).flow.cachedIn(viewModelScope)
}