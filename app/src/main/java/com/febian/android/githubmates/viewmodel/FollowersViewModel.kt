package com.febian.android.githubmates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.repository.FollowersRepository

class FollowersViewModel : ViewModel() {

    private var followersLiveData: MutableLiveData<List<User>>? = MutableLiveData()

    fun getFollowers(username: String): LiveData<List<User>>? {
        followersLiveData =
            FollowersRepository.getFollowersApiCall(username)
        return followersLiveData
    }

}