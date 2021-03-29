package com.febian.android.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val name: String?,
    val avatarUrl: String?,
    val company: String?,
    val location: String?,
    val repositoryCount: Int?,
    val followers: Int?,
    val following: Int?,
    val profileUrl: String?,
    val followersUrl: String?,
    val followingUrl: String?,
    val createdDate: String?,
) : Parcelable