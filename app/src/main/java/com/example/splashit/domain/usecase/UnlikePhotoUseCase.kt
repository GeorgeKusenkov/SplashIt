package com.example.splashit.domain.usecase

import com.example.splashit.domain.repository.CurrentUserRepository

class UnlikePhotoUseCase(private val currentUserPhotoRepository: CurrentUserRepository) {
    suspend fun execute(id: String) {
        currentUserPhotoRepository.unlikePhoto(id)
    }
}