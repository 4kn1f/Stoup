package com.submission.stoup.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.stoup.data.remote.pref.UserModel
import com.submission.stoup.data.remote.repository.UserRepository
import com.submission.stoup.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, pw: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.login(email, pw)
                if (result.isSuccess) {
                    val loginResponse = result.getOrNull()
                    if (loginResponse != null && loginResponse.loginResult?.token != null) {
                        val user = UserModel(email = email, token = loginResponse.loginResult.token, login = true
                        )
                        saveSession(user)
                        _loginResult.postValue(loginResponse)
                    } else {
                        _errorMessage.postValue("Token not found")
                    }
                } else {
                    _errorMessage.postValue(result.exceptionOrNull()?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "Error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSessions(user)
        }
    }
}
