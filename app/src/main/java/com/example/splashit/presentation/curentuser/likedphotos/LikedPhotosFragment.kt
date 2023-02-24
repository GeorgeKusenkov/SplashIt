package com.example.splashit.presentation.curentuser.likedphotos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.splashit.R
import com.example.splashit.databinding.FragmentLikedPhotosBinding
import com.example.splashit.presentation.curentuser.CurrentUserFragmentDirections

class LikedPhotosFragment : Fragment() {
    private var _binding: FragmentLikedPhotosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LikedPhotosViewModel by viewModels()
    private val adapter = LikedPhotosPageAdapter{ item -> onPhotoClicker(item)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        checkExistenceLikedPhotos()
        loadLikedPhotos()
    }

    private fun checkExistenceLikedPhotos() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.noneLikedMessage.isGone = false
                binding.recyclerView.isGone = true
            } else {
                binding.noneLikedMessage.isGone = true
                binding.recyclerView.isGone = false
            }
        }
    }

    private fun loadLikedPhotos() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.likedPhotos.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun onPhotoClicker(item: String) {
        findNavController().navigate(R.id.currentUserFragment)
        val action = CurrentUserFragmentDirections.actionCurrentUserFragmentToDetailsFragment(item)
        findNavController().navigate(action)
    }
}
