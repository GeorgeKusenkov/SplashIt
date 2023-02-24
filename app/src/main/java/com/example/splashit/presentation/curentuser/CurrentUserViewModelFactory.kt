package com.example.splashit.presentation.curentuser

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splashit.domain.usecase.CleanCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserUseCase

class CurrentUserViewModelFactory(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val cleanCurrentUserTokenUseCase: CleanCurrentUserTokenUseCase,
    val getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase,
    context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentUserViewModel(
            cleanCurrentUserTokenUseCase,
            getCurrentUserUseCase,
            getCurrentUserTokenUseCase
        ) as T
    }
}