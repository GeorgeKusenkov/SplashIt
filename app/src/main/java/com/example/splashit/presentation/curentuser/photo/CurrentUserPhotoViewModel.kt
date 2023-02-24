package com.example.splashit.presentation.curentuser.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splashit.domain.models.CollectionPhotos
import kotlinx.coroutines.flow.Flow

class CurrentUserPhotoViewModel: ViewModel() {
    val userPhoto: Flow<PagingData<CollectionPhotos>> = Pager (
        config = PagingConfig(pageSize = 1),
        pagingSourceFactory = { CurrentUserPhotoPagingSource() }
    ).flow.cachedIn(viewModelScope)
}