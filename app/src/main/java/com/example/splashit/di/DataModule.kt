package com.example.splashit.di

import android.content.Context
import com.example.splashit.data.repository.ActualTokenRepositoryImpl
import com.example.splashit.data.repository.CurrentUserRepositoryImpl
import com.example.splashit.data.repository.UnsplashPhotoRepositoryImpl
import com.example.splashit.data.storage.ActualTokenStorage
import com.example.splashit.data.storage.sharedprefs.SharedPrefsToken
import com.example.splashit.domain.repository.CurrentUserRepository
import com.example.splashit.domain.repository.CurrentUserTokenRepository
import com.example.splashit.domain.repository.UsersPhotoRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun provideCurrentUserRepository(): CurrentUserRepository{
        return CurrentUserRepositoryImpl()
    }

    @Provides
    fun provideUserStorage(context: Context): ActualTokenStorage {
       return SharedPrefsToken(context = context)
    }

    @Provides
    fun provideUserStorageRepository(actualTokenStorage: ActualTokenStorage): CurrentUserTokenRepository {
        return ActualTokenRepositoryImpl(actualTokenStorage)
    }

    @Provides
    fun provideListUsersPhoto(): UsersPhotoRepository {
        return UnsplashPhotoRepositoryImpl()
    }
}