package com.example.splashit.domain.usecase

import com.example.splashit.domain.repository.CurrentUserTokenRepository

class GetCurrentUserTokenUseCase(private val tokenRepository: CurrentUserTokenRepository) {
    fun execute(): String {
        return tokenRepository.getToken()
    }
}