package com.submission.stoup.data.remote.retrofit

import com.submission.stoup.data.remote.response.AddStoriesResponse
import com.submission.stoup.data.remote.response.DetailStoriesResponse
import com.submission.stoup.data.remote.response.LoginResponse
import com.submission.stoup.data.remote.response.RegisterResponse
import com.submission.stoup.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ) : StoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : DetailStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ) : AddStoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = 1,
    ): StoriesResponse
}