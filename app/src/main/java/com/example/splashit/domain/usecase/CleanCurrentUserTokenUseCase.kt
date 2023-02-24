package com.example.splashit.domain.usecase

import com.example.splashit.domain.repository.CurrentUserTokenRepository

class CleanCurrentUserTokenUseCase(private val tokenRepository: CurrentUserTokenRepository) {
    fun execute() {
        tokenRepository.cleanToken()
    }
}