package com.submission.stoup.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.repository.UserRepository

class HomeViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSessions().asLiveData()
    }
}