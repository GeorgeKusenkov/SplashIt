package com.example.splashit.presentation.curentuser.likedphotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splashit.domain.models.CurrentUserLikedPhotos
import kotlinx.coroutines.flow.Flow

class LikedPhotosViewModel: ViewModel() {
    val likedPhotos: Flow<PagingData<CurrentUserLikedPhotos>> = Pager (
        config = PagingConfig(pageSize = 1),
        pagingSourceFactory = { LikedUserPhotosPagingSource() }
    ).flow.cachedIn(viewModelScope)
}