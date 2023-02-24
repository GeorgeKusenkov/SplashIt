package com.example.splashit.presentation.curentuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashit.data.auth.AuthRepository
import com.example.splashit.domain.models.CurrentUserInfo
import com.example.splashit.domain.usecase.CleanCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentUserViewModel(
    private val cleanCurrentUserTokenUseCase: CleanCurrentUserTokenUseCase,
    private val getUserInfoUseCase: GetCurrentUserUseCase,
    private val getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase
) : ViewModel() {
    private val authRepository = AuthRepository()

    private var _userInfo = MutableStateFlow<CurrentUserInfo?>(null)
    val userInfo = _userInfo.asStateFlow()

    private var _showLoadingMessage = MutableStateFlow(false)
    val showLoadingMessage = _showLoadingMessage.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            runCatching {
                _isLoading.value = true
                getUserInfoUseCase.execute()
            }.onSuccess {
                _userInfo.value = getUserInfoUseCase.execute()
                _showLoadingMessage.value = false
                _isLoading.value = false
                Log.d("Oauth", "Загрузка данных: УСПЕХ")
            }.onFailure {
                _showLoadingMessage.value = true
                _isLoading.value = false
                Log.d("Oauth", "Загрузка данных: ПРОВАЛ")
            }
        }
    }

    fun logout() {
        cleanCurrentUserTokenUseCase.execute()
        authRepository.logout()
    }
}