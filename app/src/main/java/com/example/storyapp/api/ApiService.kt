package com.example.storyapp.api

import com.example.storyapp.model.FileUploadResponse

import com.example.storyapp.model.ResponseResult
import com.example.storyapp.model.StoriesResponse
import retrofit2.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("email") email: String
    ): Call<FileUploadResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseResult>

    @GET("stories")
    fun getStoriesLocation(
        @Header("Authorization")
        Auth: String,
        @Query("location") Location: Int = 1
    ) : Call<StoriesResponse>
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") Auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): Call<FileUploadResponse>

    @GET("stories")
    suspend fun getStoriesSource(
        @Header("Authorization")
        Auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") Location: Int
    ): StoriesResponse
}