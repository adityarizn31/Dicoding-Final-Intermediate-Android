package com.dicoding.sub1_appsstory.Retrofit

import com.dicoding.sub1_appsstory.Data.Login
import com.dicoding.sub1_appsstory.Data.Register
import com.dicoding.sub1_appsstory.Data.DetailStoryResp
import com.dicoding.sub1_appsstory.Data.ReceiveResp
import com.dicoding.sub1_appsstory.Data.GetStoryResp
import com.dicoding.sub1_appsstory.Data.LoginResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // Receiver Resp == Register Resp

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ReceiveResp

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResp


    @Multipart
    @POST("stories")
    suspend fun sendStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): ReceiveResp

    @GET("stories")
    suspend fun getStrories(
        @Header("Authorization")
        token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResp

    @GET("stories/{id}")
    suspend fun getStroryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResp

    // Submission 2
    @GET("stories")
    suspend fun getStoryMap(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ): GetStoryResp

}