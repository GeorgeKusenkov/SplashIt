package com.example.splashit.presentation.usercollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase

class UserCollectionViewModelFactory(
    val getLikePhotoUseCase: LikePhotoUseCase,
    val getUnlikePhotoUseCase: UnlikePhotoUseCase,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserCollectionViewModel(
            getLikePhotoUseCase,
            getUnlikePhotoUseCase,
        ) as T
    }
}
