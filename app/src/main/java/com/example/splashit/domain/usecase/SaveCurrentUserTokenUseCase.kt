package com.example.splashit.domain.usecase

import com.example.splashit.domain.repository.CurrentUserTokenRepository

class SaveCurrentUserTokenUseCase(private val tokenRepository: CurrentUserTokenRepository) {
    fun execute(token: String): Boolean {
        return tokenRepository.saveToken(token)
    }
}