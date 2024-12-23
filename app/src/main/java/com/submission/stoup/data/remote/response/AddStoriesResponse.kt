package com.submission.stoup.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddStoriesResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)