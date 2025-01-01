package com.submission.stoup.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.stoup.data.remote.repository.StoryRepository
import com.submission.stoup.data.remote.response.StoriesResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<StoriesResponse>()
    val stories: LiveData<StoriesResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStoriesWithLocation(){
        _isLoading.value = true
        viewModelScope.launch {
            val result = storyRepository.getStoriesLocation()
            result.onSuccess { response ->
                _stories.value = response
            }.onFailure { exception ->
                _errorMessage.value = exception.message
            }
            _isLoading.value = false
        }
    }
}