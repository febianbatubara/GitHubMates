package com.febian.android.githubmates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("login")
    @Expose
    val username: String?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String?,

    @SerializedName("company")
    @Expose
    val company: String?,

    @SerializedName("location")
    @Expose
    val location: String?,

    @SerializedName("public_repos")
    @Expose
    val repositoryCount: Int?,

    @SerializedName("followers")
    @Expose
    val followers: Int?,

    @SerializedName("following")
    @Expose
    val following: Int?,

    @SerializedName("html_url")
    @Expose
    val profileUrl: String?,

    @SerializedName("followers_url")
    @Expose
    val followersUrl: String?,

    @SerializedName("following_url")
    @Expose
    val followingUrl: String?,

    @SerializedName("created_at")
    @Expose
    val createdDate: String?,
)