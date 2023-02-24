package com.example.splashit.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.splashit.R
import com.example.splashit.app.App
import com.example.splashit.databinding.FragmentMainBinding
import com.example.splashit.domain.repository.Launch
import com.example.splashit.presentation.Loader
import com.example.splashit.presentation.details.ConnectivityObserver
import com.example.splashit.presentation.details.NetworkConnectivityObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalPagingApi
class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MainFragmentViewModelFactory
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var connectivityObserver: ConnectivityObserver
    private var connectivityStatus = ConnectivityObserver.Status.Unavailable
    private val usersPhotoPageAdapter = UsersPhotoPageAdapter(
        { item, pos -> onLikeClick(item, pos) },
        { item -> onPhotoClick(item) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.show()
        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            true
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.photos_label)
        checkNetworkConnection()
        setAdapter()
        loadPhotoList()
    }

    private fun setAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = usersPhotoPageAdapter
    }

    private fun checkNetworkConnection() {
        connectivityObserver = NetworkConnectivityObserver(requireContext())
        try {
            connectivityObserver.observe().onEach {
                connectivityStatus = it
            }.launchIn(lifecycleScope)
        } catch (e: Exception) {
            Log.d("NetworkConnection", e.toString())
        }

    }

    private fun loadPhotoList() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.characters.collect {
                checkDataLoading()
                usersPhotoPageAdapter.submitData(it)
            }
        }
    }

    private fun checkDataLoading() {
        usersPhotoPageAdapter.addLoadStateListener { loadStates ->
            val dataIsLoading = loadStates.refresh is LoadState.Loading
            if (dataIsLoading) {
                Loader().showProgress(binding.progressBar, binding.recyclerView)
            } else {
                Loader().hideProgress(binding.progressBar, binding.recyclerView)
                if (loadStates.refresh is LoadState.Error) {
                    if (usersPhotoPageAdapter.itemCount < 1) {
                        Loader().showMessage(binding.emptyListMessage, binding.recyclerView)
                    } else {
                        Loader().hideMessage(binding.emptyListMessage, binding.recyclerView)
                    }
                }
            }
        }
    }

    private fun onLikeClick(item: Launch, position: Int) {
        if (connectivityStatus == ConnectivityObserver.Status.Lost || connectivityStatus == ConnectivityObserver.Status.Unavailable) {
            Snackbar.make(
                binding.root,
                "Для выполнения данного действия необходимо соединение.",
                Snackbar.LENGTH_LONG
            ).show()

        } else {
            viewModel.like(item, position, usersPhotoPageAdapter)
        }
    }

    private fun onPhotoClick(item: String) {
        if (connectivityStatus == ConnectivityObserver.Status.Available) {
            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(item)
            findNavController().navigate(action)
        } else if (connectivityStatus == ConnectivityObserver.Status.Lost || connectivityStatus == ConnectivityObserver.Status.Unavailable) {
            Snackbar.make(
                binding.root,
                "Для выполнения данного действия необходимо соединение.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}