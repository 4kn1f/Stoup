package com.submission.stoup.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.stoup.data.remote.repository.StoryRepository
import com.submission.stoup.data.remote.response.DetailStoriesResponse
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val _detailStory = MutableLiveData<DetailStoriesResponse?>()
    val detailStory: LiveData<DetailStoriesResponse?> = _detailStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStoryDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = storyRepository.getDetailStory(id)
                _isLoading.value = false

                if (response.isSuccess) {
                    response.getOrNull()?.let { body ->
                        _detailStory.value = body
                    }
                } else {
                    _errorMessage.value = "Error: ${response.exceptionOrNull()?.message ?: "Error not found"}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message ?: "Error tidak diketahui"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}