package com.febian.android.githubmates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.model.UserResponse
import com.febian.android.githubmates.repository.UserRepository

class UserViewModel : ViewModel() {

    private var userLiveData: MutableLiveData<List<User>>? = MutableLiveData()
    private var searchUserLiveData: MutableLiveData<UserResponse>? = MutableLiveData()

    fun getUsers(): LiveData<List<User>>? {
        userLiveData = UserRepository.getUsersApiCall()
        return userLiveData
    }

    fun searchUser(username: String): LiveData<UserResponse>? {
        searchUserLiveData = UserRepository.searchUsersApiCall(username)
        return searchUserLiveData
    }

}