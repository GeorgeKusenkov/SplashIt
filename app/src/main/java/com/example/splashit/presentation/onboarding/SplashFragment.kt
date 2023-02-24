package com.example.splashit.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.splashit.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            false
        (activity as AppCompatActivity).supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished()) {
                findNavController().navigate(R.id.action_splashFragment_to_authFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }

        }, 3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPrefs = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("OnBoardingFinished", false)
    }
}