package com.example.splashit.presentation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.splashit.R
import com.example.splashit.databinding.FragmentCollectionsBinding
import com.example.splashit.presentation.Loader

class CollectionsFragment : Fragment() {

    private val viewModel: CollectionsViewModel by viewModels()
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val collectionsPhotoPageAdapter = CollectionsPageAdapter { onPhotoClicker(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.collections)
        binding.recyclerView.adapter = collectionsPhotoPageAdapter
        loadCollectionsList()
    }

    private fun loadCollectionsList() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.collections.collect {
                checkDataLoading()
                collectionsPhotoPageAdapter.submitData(it)
            }
        }
    }

    private fun checkDataLoading() {
        collectionsPhotoPageAdapter.addLoadStateListener { loadStates ->
            val dataIsLoading = loadStates.refresh is LoadState.Loading
            if (dataIsLoading) {
                Loader().showProgress(binding.progressBar, binding.recyclerView)
            } else {
                Loader().hideProgress(binding.progressBar, binding.recyclerView)
                if (loadStates.refresh is LoadState.Error) {
                    if (collectionsPhotoPageAdapter.itemCount < 1) {
                        Loader().showMessage(binding.emptyCollectionMessage, binding.recyclerView)
                    } else {
                        Loader().hideMessage(binding.emptyCollectionMessage, binding.recyclerView)
                    }
                }
            }
        }
    }

    private fun onPhotoClicker(item: CollectionInfoForDetails) {
        var str = ""
        item.tags.forEach {
            str += "#${it.title} "
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}