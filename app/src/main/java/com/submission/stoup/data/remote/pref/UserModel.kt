package com.submission.stoup.data.remote.pref

data class UserModel (
    val email: String,
    val token: String,
    val login: Boolean = false
)