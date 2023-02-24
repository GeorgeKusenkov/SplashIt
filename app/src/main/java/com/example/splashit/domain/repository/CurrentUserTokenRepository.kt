package com.example.splashit.domain.repository

interface CurrentUserTokenRepository {
    fun saveToken(token: String): Boolean
    fun getToken(): String
    fun cleanToken()
}