package com.example.splashit.data.repository

import com.example.splashit.data.storage.ActualTokenStorage
import com.example.splashit.domain.repository.CurrentUserTokenRepository

class ActualTokenRepositoryImpl(private val tokenStorage: ActualTokenStorage) :
    CurrentUserTokenRepository {
    override fun saveToken(token: String): Boolean {
        return tokenStorage.save(token)
    }

    override fun getToken(): String {
        return tokenStorage.get()
    }

    override fun cleanToken() {
        return tokenStorage.clean()
    }
}