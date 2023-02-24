package com.example.splashit.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.splashit.domain.usecase.CleanCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.GetCurrentUserUseCase
import com.example.splashit.domain.usecase.GetUnsplashPhotoDetailsUseCase
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.SaveCurrentUserTokenUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase
import com.example.splashit.presentation.authorization.AuthViewModelFactory
import com.example.splashit.presentation.curentuser.CurrentUserViewModelFactory
import com.example.splashit.presentation.details.DetailsFragmentViewModelFactory
import com.example.splashit.presentation.main.MainFragmentViewModelFactory
import com.example.splashit.presentation.usercollection.UserCollectionViewModelFactory
import dagger.Module
import dagger.Provides

@ExperimentalPagingApi
@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideAuthViewModelFactory(
        getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase,
        saveCurrentUserTokenUseCase: SaveCurrentUserTokenUseCase,
        context: Context
    ): AuthViewModelFactory {
        return AuthViewModelFactory(
            getCurrentUserTokenUseCase = getCurrentUserTokenUseCase,
            saveCurrentUserTokenUseCase = saveCurrentUserTokenUseCase,
            context
        )
    }
    @Provides
    fun provideCurrentUserViewModelFactory(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        cleanCurrentUserTokenUseCase: CleanCurrentUserTokenUseCase,
        getCurrentUserTokenUseCase: GetCurrentUserTokenUseCase
    ): CurrentUserViewModelFactory {
        return CurrentUserViewModelFactory(
            getCurrentUserUseCase = getCurrentUserUseCase,
            cleanCurrentUserTokenUseCase = cleanCurrentUserTokenUseCase,
            getCurrentUserTokenUseCase = getCurrentUserTokenUseCase,
            context
        )
    }

    @Provides
    fun provideMainViewModelFactory(
        likePhotoUseCase: LikePhotoUseCase,
        unlikePhotoUseCase: UnlikePhotoUseCase
    ): MainFragmentViewModelFactory {
        return MainFragmentViewModelFactory(
            getLikePhotoUseCase = likePhotoUseCase,
            getUnlikePhotoUseCase = unlikePhotoUseCase,
            context = context
        )
    }

    @Provides
    fun provideUserCollectionViewModelFactory(
        likePhotoUseCase: LikePhotoUseCase,
        unlikePhotoUseCase: UnlikePhotoUseCase
    ): UserCollectionViewModelFactory {
        return UserCollectionViewModelFactory(
            getLikePhotoUseCase = likePhotoUseCase,
            getUnlikePhotoUseCase = unlikePhotoUseCase
        )
    }

    @Provides
    fun provideDetailsFragmentViewModelFactory(
        likePhotoUseCase: LikePhotoUseCase,
        unlikePhotoUseCase: UnlikePhotoUseCase,
        getUserPhotoInfoUseCase: GetUnsplashPhotoDetailsUseCase
    ): DetailsFragmentViewModelFactory {
        return DetailsFragmentViewModelFactory(
            likePhotoUseCase = likePhotoUseCase,
            unlikePhotoUseCase = unlikePhotoUseCase,
            getUserPhotoInfoUseCase = getUserPhotoInfoUseCase
        )
    }


}