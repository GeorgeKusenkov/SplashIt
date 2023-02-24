package com.example.splashit.data.storage.sharedprefs

import android.content.Context
import android.util.Log
import com.example.splashit.data.auth.TokenStorage
import com.example.splashit.data.storage.ActualTokenStorage

private const val SHARED_PREFS_TOKEN = "shared_prefs_token"
private const val KEY_ACTUAL_TOKEN = "actual_token"
private const val DEFAULT_TOKEN = ""

class SharedPrefsToken(context: Context): ActualTokenStorage {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_TOKEN, Context.MODE_PRIVATE)

    override fun save(token: String): Boolean {
        sharedPreferences.edit().putString(KEY_ACTUAL_TOKEN, token).apply()
        TokenStorage.accessToken = token
        return true
    }

    override fun get(): String {
        val actualToken = sharedPreferences.getString(KEY_ACTUAL_TOKEN, DEFAULT_TOKEN) ?: DEFAULT_TOKEN
        TokenStorage.accessToken = actualToken
        return actualToken
    }

    override fun clean() {
        sharedPreferences.edit().putString(KEY_ACTUAL_TOKEN, DEFAULT_TOKEN).apply()
    }
}