package com.example.splashit.domain.usecase

import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.domain.repository.UsersPhotoRepository

class GetUnsplashPhotoDetailsUseCase(private val usersPhotoRepository: UsersPhotoRepository)  {
    suspend fun execute(id: String): DetailUnsplashPhoto {
        return usersPhotoRepository.getUnsplashPhotoDetails(id)
    }
}