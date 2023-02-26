package com.example.splashit.presentation.curentuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.example.splashit.R
import com.example.splashit.app.App
import com.example.splashit.databinding.FragmentCurrentUserBinding
import com.example.splashit.presentation.curentuser.collections.CurrentUserCollectionsFragment
import com.example.splashit.presentation.curentuser.likedphotos.LikedPhotosFragment
import com.example.splashit.presentation.curentuser.photo.CurrentUserPhotoFragment
import javax.inject.Inject

private const val EMPTY_STRING_VALUE = ""
private const val EMPTY_INT_VALUE = "0"

@ExperimentalPagingApi
class CurrentUserFragment : Fragment(), MenuProvider {

    @Inject
    lateinit var viewModelFactory: CurrentUserViewModelFactory
    private lateinit var viewModel: CurrentUserViewModel
    private var _binding: FragmentCurrentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[CurrentUserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.profile)
        setBottomNavigation()
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.isLoading
                    .collect {
                        if (it) {
                            binding.noConnectionMessage.isVisible = false
                            binding.progressBar.isVisible = true
                        } else {
                            binding.progressBar.isVisible = false
                            viewModel.showLoadingMessage
                                .collect { needToShowMessage ->
                                    if (needToShowMessage) {
                                        binding.noConnectionMessage.isVisible = true
                                    } else {
                                        binding.notLoadingLayout.visibility = View.GONE
                                    }
                                }
                        }
                    }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {

                viewModel.userInfo
                    .collect {
                        with(binding) {
                            userName.text =
                                "${it?.first_name ?: EMPTY_STRING_VALUE} ${it?.last_name ?: EMPTY_STRING_VALUE}"
                            nickname.text = "@${it?.username ?: EMPTY_STRING_VALUE}"

                            if (it?.bio.isNullOrEmpty())
                                status.isVisible = false
                            else
                                status.text = it?.bio ?: EMPTY_STRING_VALUE


                            if (it?.location.isNullOrEmpty()) {
                                icLocation.isVisible = false
                                location.isVisible = false
                            } else
                                location.text = it?.location ?: EMPTY_STRING_VALUE



                            eMail.text = it?.email ?: EMPTY_STRING_VALUE
                            downloads.text = it?.downloads.toString()

                            bottomNavigation.menu.findItem(R.id.currentUserPhotoFragment).title =
                                getString(
                                    R.string.current_user_downloads,
                                    it?.downloads ?: EMPTY_INT_VALUE
                                )
                            bottomNavigation.menu.findItem(R.id.likedPhotosFragment).title =
                                getString(
                                    R.string.current_user_likes, it?.totalLikes ?: EMPTY_INT_VALUE
                                )
                            bottomNavigation.menu.findItem(R.id.collections).title =
                                getString(
                                    R.string.current_user_collections,
                                    it?.totalCollections ?: EMPTY_INT_VALUE
                                )
//
                            Glide
                                .with(requireContext())
                                .load(it?.profileImage?.small)
                                .error(R.drawable.placeholder_no_image)
                                .circleCrop()
                                .into(icCurrentUser)
                        }
                    }
            }
    }

    private fun setBottomNavigation() {
        navigateToFragment(CurrentUserPhotoFragment())
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.currentUserPhotoFragment -> navigateToFragment(CurrentUserPhotoFragment())
                R.id.likedPhotosFragment -> navigateToFragment(LikedPhotosFragment())
                R.id.collections -> navigateToFragment(CurrentUserCollectionsFragment())
            }
            true
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, fragment)
            .commitNow()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.current_user_actionbar, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.currentUserLogout) {
            onAlertDialog(binding.root)
            return true
        }
        return false
    }

    private fun onAlertDialog(view: View) {
        AlertDialog.Builder(view.context)
            .setTitle("Вы действиьтельно хотите выйти?")
            .setMessage("Все локальные данные будут удалены")
            .setPositiveButton(
                "Да"
            ) { _, _ ->
                viewModel.logout()
                findNavController().navigate(R.id.authFragment)
            }
            .setNegativeButton(
                "Нет"
            ) { _, _ -> }
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}