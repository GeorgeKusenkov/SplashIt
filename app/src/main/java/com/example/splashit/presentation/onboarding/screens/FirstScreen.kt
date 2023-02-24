package com.example.splashit.presentation.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.splashit.R
import com.example.splashit.databinding.FragmentFirstScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FirstScreen : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            false
        (activity as AppCompatActivity).supportActionBar?.hide()

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)

        binding.next1.setOnClickListener{
            viewPager.currentItem = 1
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}