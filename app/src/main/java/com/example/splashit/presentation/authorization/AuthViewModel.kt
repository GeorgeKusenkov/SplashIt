package com.example.splashit.presentation.authorization

import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashit.data.auth.AuthRepository
import com.example.splashit.data.auth.TokenStorage
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.SaveCurrentUserTokenUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(
    private val authService: AuthorizationService,
    private val getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase,
    private val saveCurrentUserTokenUseCase: SaveCurrentUserTokenUseCase
) : ViewModel() {
    private val authRepository = AuthRepository()
    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val authSuccessEventChannel = Channel<Unit>(Channel.BUFFERED)
    private val loadingMutableStateFlow = MutableStateFlow(false)

    val openAuthPageFlow: Flow<Intent>
        get() = openAuthPageEventChannel.receiveAsFlow()

    val authSuccessFlow: Flow<Unit>
        get() = authSuccessEventChannel.receiveAsFlow()

    fun checkSharedToken(): String {
        return getCurrentUserTokenUseCase.execute()
    }

    fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = authRepository.getAuthRequest()
        Log.d(
            "Oauth",
            "1. Generated verifier=${authRequest.codeVerifier},challenge=${authRequest.codeVerifierChallenge}"
        )

        TokenStorage.accessToken
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest, customTabsIntent
        )

        openAuthPageEventChannel.trySendBlocking(openAuthPageIntent)
        Log.d("Oauth", "2. Open auth page: ${authRequest.toUri()}")
    }

    fun onAuthCodeFailed(exception: AuthorizationException) {
//        toastEventChannel.trySendBlocking("Аутентификация не выполнена")
        Log.d("Oauth", "Аутентификация не выполнена")
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        Log.d("Oauth", "3. Received code = ${tokenRequest.authorizationCode}")
        viewModelScope.launch {
            viewModelScope.launch {
                loadingMutableStateFlow.value = true
                runCatching {
                    Log.d("Oauth", "4. Change code to token. Url = ${tokenRequest.configuration.tokenEndpoint}, verifier = ${tokenRequest.codeVerifier}"
                    )
                    authRepository.performTokenRequest(
                        authService = authService, tokenRequest = tokenRequest
                    )
                }.onSuccess {
                    loadingMutableStateFlow.value = false
                    authSuccessEventChannel.send(Unit)
                }.onFailure {
                    loadingMutableStateFlow.value = false
                }
            }
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                saveCurrentUserTokenUseCase.execute("Bearer " + TokenStorage.accessToken)
            }.onSuccess {
                loadingMutableStateFlow.value = false
                saveCurrentUserTokenUseCase.execute("Bearer " + TokenStorage.accessToken)
            }.onFailure {
                loadingMutableStateFlow.value = false
            }
        }
    }
}