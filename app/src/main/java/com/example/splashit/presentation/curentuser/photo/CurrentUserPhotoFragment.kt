package com.example.splashit.presentation.curentuser.photo

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
import com.example.splashit.databinding.FragmentCurrentUserPhotoBinding
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.presentation.curentuser.CurrentUserFragmentDirections
import com.example.splashit.presentation.usercollection.UserCollectionPageAdapter

class CurrentUserPhotoFragment : Fragment() {
    private var _binding: FragmentCurrentUserPhotoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrentUserPhotoViewModel by viewModels()

    private val adapter = UserCollectionPageAdapter(
        { item, pos -> onLikeClick(item, pos) },
        { item -> onPhotoClick(item) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentUserPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        checkExistencePhoto()
        loadCurrentUserPhotos()
    }

    private fun loadCurrentUserPhotos() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userPhoto.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun checkExistencePhoto() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.message.isGone = false
                binding.recyclerView.isGone = true
            } else {
                binding.message.isGone = true
                binding.recyclerView.isGone = false
            }
        }
    }

    private fun onPhotoClick(item: String) {
        findNavController().navigate(R.id.currentUserFragment)
        val action = CurrentUserFragmentDirections.actionCurrentUserFragmentToDetailsFragment(item)
        findNavController().navigate(action)
    }

    private fun onLikeClick(item: CollectionPhotos, position: Int) {
//        viewModel.like(item, position, adapter)
    }

}