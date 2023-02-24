package com.example.splashit.presentation.usercollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserCollectionViewModel(
    private val likePhotoUseCase: LikePhotoUseCase,
    private val unlikePhotoUseCase: UnlikePhotoUseCase,
    ): ViewModel() {
    var args = ""
    val collectionPhotos: Flow<PagingData<CollectionPhotos>> = Pager (
        config = PagingConfig(pageSize = 1),
        pagingSourceFactory = { UserCollectionPagingSource(args) }
    ).flow.cachedIn(viewModelScope)

    fun like(item: CollectionPhotos, position: Int, usersPhotoPageAdapter: UserCollectionPageAdapter) {
        val photoId = item.id
        viewModelScope.launch {
            kotlin.runCatching {
                likePhotoUseCase.execute(photoId)
            }.fold(
                onSuccess = {
                    with(item) {
                        if (!likedByUser) {
                            likePhotoUseCase.execute(photoId)
                            likedByUser = true
                            totalLikes++
                            usersPhotoPageAdapter.notifyItemChanged(position, Unit)
                        } else {
                            unlikePhotoUseCase.execute(photoId)
                            likedByUser = false
                            totalLikes--
                            usersPhotoPageAdapter.notifyItemChanged(position, Unit)
                        }
                    }

                },
                onFailure = {
                }
            )
        }
    }
}