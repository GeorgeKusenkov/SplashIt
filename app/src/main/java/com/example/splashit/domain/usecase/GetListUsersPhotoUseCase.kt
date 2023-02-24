package com.example.splashit.domain.usecase

import com.example.splashit.domain.models.UsersPhoto
import com.example.splashit.domain.repository.UsersPhotoRepository

class GetListUsersPhotoUseCase(private val usersPhotoRepository: UsersPhotoRepository) {
    suspend fun execute(page: Int): List<UsersPhoto> {
        return usersPhotoRepository.getListUnsplashPhoto(page = page)
    }
}