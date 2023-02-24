package com.example.splashit.presentation.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.splashit.R
import com.example.splashit.databinding.FragmentSecondScreenBinding

class SecondScreen : Fragment() {
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)


        binding.next2.setOnClickListener {
            viewPager.currentItem = 2
        }

        binding.back2.setOnClickListener {
            viewPager.currentItem = 0
        }


        return  binding.root
    }
}