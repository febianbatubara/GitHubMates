package com.febian.android.githubmates.api

import com.febian.android.githubmates.BuildConfig
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUsers(): Call<List<User>>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun searchByUsername(@Query("q") username: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUserDetail(@Path("username") username: String): Call<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowersList(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowingList(@Path("username") username: String): Call<List<User>>

}