package com.submission.stoup.data.remote.repository

import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.pref.UserPreferences
import com.submission.stoup.data.remote.response.LoginResponse
import com.submission.stoup.data.remote.response.RegisterResponse
import com.submission.stoup.data.remote.retrofit.ApiService
import kotlin.Result
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val userPreferences: UserPreferences, private val apiService: ApiService){

    suspend fun register(name: String, email: String, pw: String): Result<RegisterResponse>{
        return try {
            val response = apiService.register(name, email, pw)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun login(email: String, pw: String): Result<LoginResponse>{
        return try {
            val response =  apiService.login(email, pw)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun saveSessions(user: UserModel){
        userPreferences.saveSessions(user)
    }

    fun getSessions(): Flow<UserModel>{
        return userPreferences.getSessions()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreferences: UserPreferences,
            apiService: ApiService
        ): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(userPreferences, apiService)
                    .also { instance = it }
            }
        }
    }
}