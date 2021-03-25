package com.febian.android.githubmates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("items")
    @Expose
    val users: List<User>? = null
)