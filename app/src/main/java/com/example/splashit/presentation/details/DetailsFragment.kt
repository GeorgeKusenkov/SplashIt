package com.example.splashit.presentation.details

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.splashit.R
import com.example.splashit.app.App
import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@ExperimentalPagingApi
class DetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: DetailsFragmentViewModelFactory
    private lateinit var viewModel: DetailsViewModel
    private val args: DetailsFragmentArgs by navArgs()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var downloadId = 0L
    private lateinit var br: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[DetailsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle()
        getIdFromArgs()
        checkLoadingState()
        loadData()
        setOnClickListener()


    }

    private fun getIdFromArgs() {
        viewModel.getUserInfo(args.id)
    }

    private fun setActionBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.photos)
    }

    private fun setOnClickListener() {
        binding.toggleButton.setOnClickListener {
            likePhoto()
        }
    }

    private fun likePhoto() {
        viewModel.like(args.id)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.numbersOfLikes.collect {
                binding.numberOfLikes.text = it.toString()
            }
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userInfo.collect {
                binding.userLocation.text =
                    it?.location?.city?.toString() ?: getString(R.string.no_information)
                binding.photoInfo.text = getString(
                    R.string.photo_info,
                    it?.exif?.make ?: getString(R.string.no_information),
                    it?.exif?.model ?: getString(R.string.no_information),
                    it?.exif?.exposure_time ?: getString(R.string.no_information),
                    it?.exif?.aperture ?: getString(R.string.no_information),
                    it?.exif?.focal_length ?: getString(R.string.no_information),
                    it?.exif?.iso ?: getString(R.string.no_information)
                )
                binding.userInfo.text = getString(
                    R.string.user_info,
                    it?.user?.username ?: getString(R.string.no_information),
                    it?.user?.bio ?: getString(R.string.no_information)
                )
                it?.let { photoInfo ->
                    val tags = {
                        var str = ""
                        photoInfo.tags.forEach { tag ->
                            str += "#${tag.title} "
                        }
                        str
                    }

                    binding.userName.text = photoInfo.user.name
                    binding.userUrl.text = photoInfo.user.username
                    binding.numberOfLikes.text = photoInfo.likes.toString()

                    if (tags().isEmpty())
                        binding.photoTags.isGone = true
                    else
                        binding.photoTags.text = tags()

                    val drawable = CircularProgressDrawable(requireContext())
                    drawable.setColorSchemeColors(
                        R.color.purple_200,
                        R.color.purple_500,
                        R.color.teal_700
                    )
                    drawable.centerRadius = 30f
                    drawable.strokeWidth = 5f
                    drawable.start()

                    binding.numberOfDownloads.text = " (${photoInfo.downloads})"
                    binding.toggleButton.isChecked = photoInfo.liked_by_user == true

                    Glide
                        .with(binding.userImage.context)
                        .load(photoInfo.urls.regular)
                        .override(300, 400)
                        .placeholder(drawable)
                        .error(R.drawable.placeholder_no_image)
                        .into(binding.userImage)

                    Glide
                        .with(binding.userAvatar.context)
                        .load(photoInfo.user.profile_image.medium)
                        .circleCrop()
                        .into(binding.userAvatar)

                    binding.download.setOnClickListener {
                        loadPhoto(photoInfo)
                    }

                    binding.icDownload.setOnClickListener {
                        loadPhoto(photoInfo)
                    }
                }
            }
        }
    }

    private fun loadPhoto(photoInfo: DetailUnsplashPhoto) {
        downloadImage(photoInfo.urls.raw, photoInfo.id)
        Snackbar.make(
            binding.root,
            resources.getString(R.string.loading_photo),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun checkLoadingState() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.isLoading
                    .collect { isLoading ->
                        if (isLoading) {
                            binding.noConnectionMessage.isVisible = false
                            binding.progressBar.isVisible = true
                            binding.notLoadingLayout.isVisible = false
                        } else {
                            binding.progressBar.isVisible = false
                            viewModel.showLoadingMessage
                                .collect { needToShowMessage ->
                                    if (needToShowMessage) {
                                        binding.notLoadingLayout.isVisible = true
                                        binding.noConnectionMessage.isVisible = true
                                    } else {
                                        binding.notLoadingLayout.isVisible = false
                                    }
                                }
                        }
                    }
            }
    }

    private fun downloadImage(url: String, title: String) {
        val fileName = "unsplash_$title.jpeg"
        viewLifecycleOwner.lifecycleScope.launch {
            val request = DownloadManager.Request(Uri.parse(url))
                .setMimeType("image/jpeg")
                .setTitle(fileName)
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    File.separator + fileName
                )
            val dm = requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadId = dm.enqueue(request)

            br = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
                            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                            val query = DownloadManager.Query().setFilterById(id)
                            val dwlmng =
                                requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                            val cur = dwlmng.query(query)
                            if (cur.moveToFirst()) {
                                val index = cur.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                if (DownloadManager.STATUS_SUCCESSFUL == cur.getInt(index)) {
                                    Snackbar.make(
                                        binding.root,
                                        "Фото загружено",
                                        Snackbar.LENGTH_LONG
                                    ).setAction("открыть") {
                                        openFile()
                                    }.show()
                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        "Не удалось загрузить",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }
                    } catch (e: Exception) {
                        Log.i("DOWNLOAD_STATUS", "Exception: $e!")
                    }
                }
            }
            requireContext().registerReceiver(
                br,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }
    }

    fun openFile() {
        val install = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        startActivity(install)
    }
//    fun openWithFileProvider(file: File) {
//        val url =
//            FileProvider.getUriForFile(
//                requireContext(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                file
//            )
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.setDataAndType(url, "image/jpeg")
//        startActivity(intent)
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}