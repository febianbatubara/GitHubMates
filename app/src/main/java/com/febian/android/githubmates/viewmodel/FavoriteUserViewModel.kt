package com.febian.android.githubmates.viewmodel

import androidx.lifecycle.ViewModel
import com.febian.android.githubmates.repository.FavoriteUserRepository

class FavoriteUserViewModel : ViewModel() {

    private val favoriteUserRepository = FavoriteUserRepository.get()

    val favoriteUsers = favoriteUserRepository.getFavoriteUsers()

}