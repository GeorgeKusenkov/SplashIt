package com.example.splashit.domain.usecase

import com.example.splashit.domain.repository.CurrentUserRepository

class LikePhotoUseCase(private val currentUserPhotoRepository: CurrentUserRepository) {
    suspend fun execute(id: String) {
        currentUserPhotoRepository.likePhoto(id)
    }
}