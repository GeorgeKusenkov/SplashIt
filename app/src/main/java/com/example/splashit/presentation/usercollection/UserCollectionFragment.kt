package com.example.splashit.presentation.usercollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.example.splashit.app.App
import com.example.splashit.databinding.FragmentUserCollectoinBinding
import com.example.splashit.domain.models.CollectionPhotos
import com.example.splashit.presentation.Loader
import javax.inject.Inject

@ExperimentalPagingApi
class UserCollectionFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: UserCollectionViewModelFactory
    private lateinit var viewModel: UserCollectionViewModel
    private val args: UserCollectionFragmentArgs by navArgs()
    private var _binding: FragmentUserCollectoinBinding? = null
    private val binding get() = _binding!!
    private val adapter = UserCollectionPageAdapter(
        { item, pos -> onLikeClick(item, pos) },
        { item -> onPhotoClick(item) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserCollectoinBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[UserCollectionViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = args.title
        setHeader()
        loadCollections()
    }

    private fun setHeader() {
        viewModel.args = args.collectionId
        binding.recyclerView.adapter = adapter
        binding.description.text = args.description
        binding.title.text = args.title
        binding.tags.text = args.tags
        binding.numbersOfPhoto.text = "${args.totalPhotos} images by @${args.author}"

        Glide
            .with(binding.backgroundImage.context)
            .load(args.coverPhoto)
            .into(binding.backgroundImage)
    }

    private fun loadCollections() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.collectionPhotos.collect {
                checkDataLoading()
                adapter.submitData(it)
            }
        }
    }

    private fun checkDataLoading() {
        adapter.addLoadStateListener { loadStates ->
            val dataIsLoading = loadStates.refresh is LoadState.Loading
            if (dataIsLoading) {
                Loader().showProgress(binding.progressBar, binding.recyclerView)
            } else {
                Loader().hideProgress(binding.progressBar, binding.recyclerView)
                if (loadStates.refresh is LoadState.Error) {
                    if (adapter.itemCount < 1) {
                        Loader().showMessage(binding.emptyCollectionMessage, binding.recyclerView)
                    } else {
                        Loader().hideMessage(binding.emptyCollectionMessage, binding.recyclerView)
                    }
                }
            }
        }
    }

    private fun onPhotoClick(item: String) {
        val action =
            UserCollectionFragmentDirections.actionUserCollectoinFragmentToDetailsFragment(item)
        findNavController().navigate(action)
    }

    private fun onLikeClick(item: CollectionPhotos, position: Int) {
            viewModel.like(item, position, adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}