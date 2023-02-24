package com.example.splashit.data.storage

interface ActualTokenStorage {
    fun save(token: String): Boolean
    fun get(): String
    fun clean()
}