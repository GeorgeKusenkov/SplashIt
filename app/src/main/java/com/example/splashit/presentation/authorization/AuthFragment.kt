package com.example.splashit.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.example.splashit.R
import com.example.splashit.app.App
import com.example.splashit.databinding.FragmentAuthBinding
import com.example.splashit.utils.launchAndCollectIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject

@ExperimentalPagingApi
class AuthFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    private lateinit var viewModel: AuthViewModel
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val dataIntent = it.data ?: return@registerForActivityResult
            handleAuthResponseIntent(dataIntent)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]

        (activity as AppCompatActivity).supportActionBar?.hide()
        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            false

        if (viewModel.checkSharedToken().isEmpty()) {
            binding.loginButton.setOnClickListener {
                viewModel.openLoginPage()
            }
            viewModel.openAuthPageFlow.launchAndCollectIn(viewLifecycleOwner) {
                openAuthPage(it)
            }
            viewModel.authSuccessFlow.launchAndCollectIn(viewLifecycleOwner) {
                viewModel.loadUserInfo()
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
            }
        } else
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
    }

    private fun openAuthPage(intent: Intent) {
        getAuthResponse.launch(intent)
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        // пытаемся получить ошибку из ответа. null - если все ок
        val exception = AuthorizationException.fromIntent(intent)
        // пытаемся получить запрос для обмена кода на токен, null - если произошла ошибка
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        when {
            // авторизация завершались ошибкой
            exception != null -> viewModel.onAuthCodeFailed(exception)
            // авторизация прошла успешно, меняем код на токен
            tokenExchangeRequest != null ->
                viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation).isVisible =
            true
        _binding = null
    }
}