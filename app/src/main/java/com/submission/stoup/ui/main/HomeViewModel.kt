package com.submission.stoup.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.repository.StoryRepository
import com.submission.stoup.data.remote.repository.UserRepository
import com.submission.stoup.data.remote.response.StoriesResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<Result<StoriesResponse>>()
    val stories: LiveData<Result<StoriesResponse>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = storyRepository.getAllStories()
            _stories.value = result
            _isLoading.value = false
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSessions().asLiveData()
    }
}