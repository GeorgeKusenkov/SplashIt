package com.example.splashit.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splashit.domain.usecase.GetUnsplashPhotoDetailsUseCase
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase

class DetailsFragmentViewModelFactory(
    val likePhotoUseCase: LikePhotoUseCase,
    val unlikePhotoUseCase: UnlikePhotoUseCase,
    val getUserPhotoInfoUseCase: GetUnsplashPhotoDetailsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(
            likePhotoUseCase,
            unlikePhotoUseCase,
            getUserPhotoInfoUseCase
        ) as T
    }
}
