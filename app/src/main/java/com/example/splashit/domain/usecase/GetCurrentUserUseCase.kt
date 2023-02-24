package com.example.splashit.domain.usecase

import com.example.splashit.domain.models.CurrentUserInfo
import com.example.splashit.domain.repository.CurrentUserRepository

class GetCurrentUserUseCase(private val currentUserRepository: CurrentUserRepository) {
    suspend fun execute(): CurrentUserInfo {
        return currentUserRepository.getCurrentUserData()
    }
}