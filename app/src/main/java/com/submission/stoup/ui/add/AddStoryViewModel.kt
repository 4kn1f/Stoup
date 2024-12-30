package com.submission.stoup.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.stoup.data.remote.repository.StoryRepository
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel(){

    private val _storyAdd = MutableLiveData<Result<Boolean>>()
    val storyAdd: LiveData<Result<Boolean>> = _storyAdd

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun addStory(description: String, imageFile: File, latitude: Double? = null, longitude: Double? = null){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = storyRepository.addStory(description, imageFile, latitude, longitude)
                if (result.isSuccess){
                    _storyAdd.value = Result.success(true)
                }else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error"
                }
            }catch (e: Exception){
                _storyAdd.value = Result.failure(e)
            }
        }
    }
}