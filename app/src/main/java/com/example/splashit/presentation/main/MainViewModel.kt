package com.example.splashit.presentation.main

//import com.example.splashit.data.room.LaunchRemoteMediator
//import com.example.splashit.data.room.UsersPhotoDao
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splashit.app.App
import com.example.splashit.data.api.Api
import com.example.splashit.data.room.LaunchRemoteMediator
import com.example.splashit.domain.repository.Launch
import com.example.splashit.domain.usecase.LikePhotoUseCase
import com.example.splashit.domain.usecase.UnlikePhotoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class MainViewModel(
    private val likePhotoUseCase: LikePhotoUseCase,
    private val unlikePhotoUseCase: UnlikePhotoUseCase,
    context: Context
) : ViewModel() {

    private val usersPhotoDao = (context.applicationContext as App).db.usersPhotoDao()
    private val remMediator = LaunchRemoteMediator(
        launchesDao = usersPhotoDao,
        launchesApi = Api.retrofit
    )

    val characters: Flow<PagingData<Launch>> = Pager(
        config = PagingConfig(pageSize = 1),
        remoteMediator = remMediator,
        pagingSourceFactory = { usersPhotoDao.getAll() }
    ).flow.cachedIn(viewModelScope).map { it as PagingData<Launch>}

    fun like(item: Launch, position: Int, usersPhotoPageAdapter: UsersPhotoPageAdapter) {
        val photoId = item.id
        viewModelScope.launch {
            kotlin.runCatching {
                likePhotoUseCase.execute(photoId)
            }.fold(
                onSuccess = {
                    with(item) {
                        if (!likedByUser) {
                            likePhotoUseCase.execute(photoId)
                            likedByUser = true
                            likes++
                            usersPhotoPageAdapter.notifyItemChanged(position, Unit)
                        } else {
                            unlikePhotoUseCase.execute(photoId)
                            likedByUser = false
                            likes--
                            usersPhotoPageAdapter.notifyItemChanged(position, Unit)
                        }
                    }

                },
                onFailure = {
                }
            )
        }
    }
}