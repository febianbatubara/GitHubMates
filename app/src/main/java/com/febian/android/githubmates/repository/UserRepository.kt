package com.febian.android.githubmates.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.febian.android.githubmates.activity.Application
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {

    val userList = MutableLiveData<List<User>>()
    val searchUserList = MutableLiveData<UserResponse>()

    fun getUsersApiCall(): MutableLiveData<List<User>> {
        val call = RetrofitService.apiInterface.getUsers()
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(Application.appContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
        return userList
    }

    fun searchUsersApiCall(username: String): MutableLiveData<UserResponse> {
        val call = RetrofitService.apiInterface.searchByUsername(username)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    searchUserList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(Application.appContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
        return searchUserList
    }

}