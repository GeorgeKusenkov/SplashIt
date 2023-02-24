package com.example.splashit.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.splashit.R
import com.example.splashit.databinding.FragmentViewPagerBinding
import com.example.splashit.presentation.onboarding.screens.FirstScreen
import com.example.splashit.presentation.onboarding.screens.SecondScreen
import com.example.splashit.presentation.onboarding.screens.ThirdScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater)

        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            false
        (activity as AppCompatActivity).supportActionBar?.hide()


        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}