package com.example.splashit.data.api

import com.example.splashit.data.auth.TokenStorage
import com.example.splashit.data.models.collectionphoto2.CollectionPhotosTest
import com.example.splashit.data.models.collections.CollectionsList2
import com.example.splashit.data.models.currentuser.CurrentUserDto
import com.example.splashit.data.models.currentuserlikedphotos.CurrentUserLikedPhotosDto
import com.example.splashit.data.models.userinfo.DetailUnsplashPhoto
import com.example.splashit.data.models.userphotos.UserPhotosDto
import com.example.splashit.data.models.usersphoto.UsersPhotoDto
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.unsplash.com/"

interface Api {
    @GET("me")
    suspend fun getCurrentUserInfo(
    ): CurrentUserDto

    @GET("users/{username}/likes")
    suspend fun getCurrentUserLikedPhotos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): CurrentUserLikedPhotosDto

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): UserPhotosDto

    @GET("photos")
    suspend fun getListUsersPhoto(
        @Query("page") page: Int
    ): UsersPhotoDto

    @GET("photos/{id}")
    suspend fun getUnsplashPhotoDetails(
        @Path("id") id: String
    ): DetailUnsplashPhoto

    @POST("photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String
    ): CurrentUserDto

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(
        @Path("id") id: String
    ): CurrentUserDto

    @GET("collections")
    suspend fun getListOfCollections(
        @Query("page") page: Int
    ): CollectionsList2

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int
    ): CollectionPhotosTest

    @GET("users/{username}/collections")
    suspend fun getCurrentUserListCollections(
        @Path("username") username: String,
        @Query("page") page: Int
    ): CollectionsList2

    companion object {
        val retrofit: Api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${TokenStorage.accessToken}")
                    .build()
                chain.proceed(request)
            }.build())
            .build()
            .create(Api::class.java)
    }
}