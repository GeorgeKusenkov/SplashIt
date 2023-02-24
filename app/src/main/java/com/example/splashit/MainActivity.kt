package com.example.splashit

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.example.splashit.databinding.ActivityMainBinding
import com.example.splashit.presentation.details.NetworkConnectivityObserver
import com.example.splashit.presentation.details.NetworkConnectivityObserver.Companion.connectivityObserver
import com.example.splashit.presentation.details.NetworkConnectivityObserver.Companion.connectivityStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.mainBottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment!!.findNavController()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.unsplash_green)))
        NavigationUI.setupWithNavController(navView, navController, false)
        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        connectivityObserver = NetworkConnectivityObserver(this)
        try {
            connectivityObserver.observe().onEach {
                connectivityStatus = it
                NetworkConnectivityObserver.connectionStatus(
                    message = "Нет соединения. Некоторые функции приложения могут быть ограничены",
                    status = it,
                    context = binding.root
                )
            }.launchIn(lifecycleScope)
        } catch (e: Exception) {
            Log.d("DOWNLOAD_STATUS", "Exception: $e!")
        }

    }
}