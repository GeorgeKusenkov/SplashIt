package com.example.splashit.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase

@ExperimentalPagingApi
class MainFragmentViewModelFactory(
    val getLikePhotoUseCase: LikePhotoUseCase,
    val getUnlikePhotoUseCase: UnlikePhotoUseCase,
    val context: Context
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getLikePhotoUseCase,
            getUnlikePhotoUseCase,
            context
        ) as T
    }
}