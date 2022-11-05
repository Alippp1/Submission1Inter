package com.example.submission1inter.data.api

import com.example.submission1inter.akun.login.LoginData
import com.example.submission1inter.akun.login.LoginResponse
import com.example.submission1inter.akun.register.RegisterData
import com.example.submission1inter.akun.register.RegisterResponse
import com.example.submission1inter.model.DetailStoryResponse
import com.example.submission1inter.model.Story
import com.example.submission1inter.model.StoryResponse
import com.example.submission1inter.upload.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun UserRegister(
        @Field ("name") name :String,
        @Field ("email") email :String,
        @Field ("password") password :String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun UserLogin(
        @Field ("email") email :String,
        @Field ("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun stories(
        @Header("Authorization") token:String
    ):Call<StoryResponse>

 @GET("stories/{id}")
    fun Getstories(
        @Header("Authorization") token:String,
        @Path("id") id : String
    ):Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun upload(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<UploadStoryResponse>
}
