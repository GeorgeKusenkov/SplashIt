package com.example.splashit.presentation.curentuser.collections

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
import com.example.splashit.databinding.FragmentCurrentUserCollectionsBinding
import com.example.splashit.presentation.collections.CollectionInfoForDetails
import com.example.splashit.presentation.collections.CollectionsFragmentDirections
import com.example.splashit.presentation.collections.CollectionsPageAdapter
import com.example.splashit.presentation.collections.CollectionsViewModel

class CurrentUserCollectionsFragment : Fragment() {
    private var _binding: FragmentCurrentUserCollectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CollectionsViewModel by viewModels()
    private val adapter = CollectionsPageAdapter{ item -> onPhotoClicker(item)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentUserCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        checkExistenceCollections()
        loadCollections()
    }

    private fun checkExistenceCollections() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.emptyCollectionMessage.isGone = false
                binding.recyclerView.isGone = true
            } else {
                binding.emptyCollectionMessage.isGone = true
                binding.recyclerView.isGone = false
            }
        }
    }

    private fun loadCollections() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentUserCollections.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun onPhotoClicker(item: CollectionInfoForDetails) {
        var str = ""
        item.tags.forEach {
            str+="#${it.title} "
        }

        findNavController().navigate(R.id.collectionsFragment)
        val action =
            CollectionsFragmentDirections.actionCollectionsFragmentToUserCollectoinFragment(
                item.id,
                str,
                item.author,
                item.title,
                item.description,
                item.totalPhotos,
                item.coverPhoto
            )
        findNavController().navigate(action)
    }
}