package com.submission.stoup.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.submission.stoup.data.paging.StoryPagingSource
import com.submission.stoup.data.remote.pref.UserPreferences
import com.submission.stoup.data.remote.response.AddStoriesResponse
import com.submission.stoup.data.remote.response.DetailStoriesResponse
import com.submission.stoup.data.remote.response.ListStoryItem
import com.submission.stoup.data.remote.response.StoriesResponse
import com.submission.stoup.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun getAllStories(): Result<StoriesResponse> {
        return try {
            val token = userPreferences.getSessions().first().token
            if (token.isEmpty()) throw Exception("Invalid token")

            val storiesResponse = apiService.getStories("Bearer $token")
            Result.success(storiesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailStory(id: String): Result<DetailStoriesResponse> {
        return try {
            val token = userPreferences.getSessions().first().token
            val detailStoriesResponse = apiService.getDetailStories("Bearer $token", id)
            Result.success(detailStoriesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStoriesLocation(): Result<StoriesResponse> {
        return try {
            val token = userPreferences.getSessions().first().token
            if (token.isEmpty()) throw Exception("Invalid token")

            val locationResponse = apiService.getStoriesWithLocation("Bearer $token")
            Result.success(locationResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
        val token = runBlocking {
            userPreferences.getSessions().first().token
        }
        if (token.isEmpty()) throw Exception("Token tidak valid")

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun addStory(description: String, imageFile: File, latitude: Double? = null, longitude: Double? = null): Result<AddStoriesResponse> {
        return try {
            val token = userPreferences.getSessions().first().token
            if (token.isEmpty()) throw Exception("Invalid token")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestLat = latitude?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestLon = longitude?.toString()?.toRequestBody("text/plain".toMediaType())

            val addStoriesResponse = apiService.addNewStory(
                "Bearer $token", multipartBody, requestDescription, requestLat, requestLon
            )

            Result.success(addStoriesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, userPreferences: UserPreferences): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreferences).also { instance = it }
            }
        }
    }
}
