package com.example.splashit.presentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.domain.usecase.GetUnsplashPhotoDetailsUseCase
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val likePhotoUseCase: LikePhotoUseCase,
    private val unlikePhotoUseCase: UnlikePhotoUseCase,
    private val getUserPhotoInfoUseCase: GetUnsplashPhotoDetailsUseCase
) : ViewModel() {
    private var _userInfo = MutableStateFlow<DetailUnsplashPhoto?>(null)
    val userInfo = _userInfo.asStateFlow()

    private var _numbersOfLikes = MutableStateFlow(0)
    val numbersOfLikes = _numbersOfLikes.asStateFlow()

    private var _showLoadingMessage = MutableStateFlow(false)
    val showLoadingMessage = _showLoadingMessage.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    var like = 0

    fun getUserInfo(id: String) {
        viewModelScope.launch {
            runCatching {
                _isLoading.value = true
                _userInfo.value = getUserPhotoInfoUseCase.execute(id)
            }.onSuccess {
                _isLoading.value = false
                _showLoadingMessage.value = false
                _numbersOfLikes.value = _userInfo.value?.likes!!
                like = _userInfo.value?.likes!!
            }.onFailure {
                _isLoading.value = false
                _showLoadingMessage.value = true
            }
        }
    }

    fun like(photoId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                likePhotoUseCase.execute(photoId)
            }.fold(
                onSuccess = {
                    if (!userInfo.value?.liked_by_user!!) {
                        likePhotoUseCase.execute(photoId)
                        _numbersOfLikes.value = ++like
                        _userInfo.value?.liked_by_user = true
                    } else {
                        unlikePhotoUseCase.execute(photoId)
                        _numbersOfLikes.value = --like
                        _userInfo.value?.liked_by_user = false

                    }
                },
                onFailure = {
                }
            )
        }
    }
}