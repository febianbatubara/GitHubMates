package com.febian.android.githubmates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.repository.FollowingRepository

class FollowingViewModel : ViewModel() {

    private var followingLiveData: MutableLiveData<List<User>>? = MutableLiveData()

    fun getFollowing(username: String): LiveData<List<User>>? {
        followingLiveData =
            FollowingRepository.getFollowingApiCall(username)
        return followingLiveData
    }

}