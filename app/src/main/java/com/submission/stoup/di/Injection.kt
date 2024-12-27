package com.submission.stoup.di

import android.content.Context
import com.submission.stoup.data.remote.pref.UserPreferences
import com.submission.stoup.data.remote.pref.dataStore
import com.submission.stoup.data.remote.repository.StoryRepository
import com.submission.stoup.data.remote.repository.UserRepository
import com.submission.stoup.data.remote.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository{
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreferences.getInstance(context.dataStore)
        return StoryRepository.getInstance(apiService, pref)
    }
}