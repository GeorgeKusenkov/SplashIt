package com.example.splashit.di

import com.example.splashit.domain.repository.CurrentUserRepository
import com.example.splashit.domain.repository.CurrentUserTokenRepository
import com.example.splashit.domain.repository.UsersPhotoRepository
import com.example.splashit.domain.usecase.CleanCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserUseCase
import com.example.splashit.domain.usecase.GetListUsersPhotoUseCase
import com.example.splashit.domain.usecase.GetUnsplashPhotoDetailsUseCase
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.SaveCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideGetCurrentUserUseCase(repository: CurrentUserRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(currentUserRepository = repository)
    }

    @Provides
    fun provideGetCurrentUserTokenUseCase(tokenRepository: CurrentUserTokenRepository): GetCurrentUserTokenUseCase {
        return GetCurrentUserTokenUseCase(tokenRepository)
    }

    @Provides
    fun provideSaveCurrentUserTokenUseCase(tokenRepository: CurrentUserTokenRepository): SaveCurrentUserTokenUseCase {
        return SaveCurrentUserTokenUseCase(tokenRepository = tokenRepository)
    }

    @Provides
    fun provideCleanCurrentUserTokenUseCase(tokenRepository: CurrentUserTokenRepository): CleanCurrentUserTokenUseCase {
        return CleanCurrentUserTokenUseCase(tokenRepository)
    }

    @Provides
    fun provideLikePhotoUseCase(repository: CurrentUserRepository): LikePhotoUseCase {
        return LikePhotoUseCase(repository)
    }

    @Provides
    fun provideUnlikePhotoUseCase(repository: CurrentUserRepository): UnlikePhotoUseCase {
        return UnlikePhotoUseCase(repository)
    }

    @Provides
    fun provideGetListUsersPhotoUseCase(repository: UsersPhotoRepository): GetListUsersPhotoUseCase {
        return GetListUsersPhotoUseCase(repository)
    }

    @Provides
    fun provideGetUnsplashPhotoDetailsUseCase(repository: UsersPhotoRepository): GetUnsplashPhotoDetailsUseCase {
        return GetUnsplashPhotoDetailsUseCase(repository)
    }

}