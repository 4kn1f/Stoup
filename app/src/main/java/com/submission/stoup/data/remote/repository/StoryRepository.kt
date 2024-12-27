package com.submission.stoup.data.remote.repository

import com.submission.stoup.data.remote.pref.UserPreferences
import com.submission.stoup.data.remote.response.DetailStoriesResponse
import com.submission.stoup.data.remote.response.StoriesResponse
import com.submission.stoup.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class StoryRepository private constructor(private val apiService: ApiService, private val userPreferences: UserPreferences) {

    suspend fun getAllStories(): Result<StoriesResponse> {
        return try {
            val token = userPreferences.getSessions().first().token
            if (token.isEmpty()) throw Exception("Token tidak valid")

            val storiesResponse = apiService.getStories("Bearer $token")
            Result.success(storiesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailStory(id: String): Result<DetailStoriesResponse>{
        return try {
            val token = userPreferences.getSessions().first().token
            val detailStoriesResponse = apiService.getDetailStories("Bearer $token", id)
            Result.success(detailStoriesResponse)
        }catch (e: Exception) {
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