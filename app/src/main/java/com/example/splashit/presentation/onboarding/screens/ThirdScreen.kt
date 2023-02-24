package com.example.splashit.presentation.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.splashit.R
import com.example.splashit.databinding.FragmentThirdScreenBinding

class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdScreenBinding.inflate(inflater, container, false)
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        binding.finish.setOnClickListener{
            findNavController().navigate(R.id.action_viewPagerFragment_to_authFragment)
            onBoardingFinished()
        }
        binding.back3.setOnClickListener {
            viewPager.currentItem = 1
        }
        return binding.root
    }

    private fun onBoardingFinished() {
        val sharedPrefs = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("OnBoardingFinished", true)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}