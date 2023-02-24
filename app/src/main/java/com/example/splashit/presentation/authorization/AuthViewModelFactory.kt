package com.example.splashit.presentation.authorization

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.SaveCurrentUserTokenUseCase
import net.openid.appauth.AuthorizationService

class AuthViewModelFactory(
    val getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase,
    val saveCurrentUserTokenUseCase: SaveCurrentUserTokenUseCase,
    context: Context
): ViewModelProvider.Factory {
    private val authService: AuthorizationService = AuthorizationService(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            authService,
            getCurrentUserTokenUseCase,
            saveCurrentUserTokenUseCase
        ) as T
    }
}