package com.febian.android.githubmates.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.repository.FavoriteUserRepository

class FavoriteUserViewModel : ViewModel() {

    private val favoriteUserRepository = FavoriteUserRepository.get()

    fun getFavoriteUsers(context: Context) = favoriteUserRepository.getFavoriteUsers(context)

    fun checkFavoriteUser(id: Int, context: Context) =
        favoriteUserRepository.checkFavoriteUser(id, context)

    fun addToFavoriteUsers(user: User, context: Context) =
        favoriteUserRepository.addToFavorite(user, context)

    fun removeFromFavoriteUsers(user: User, context: Context) =
        favoriteUserRepository.removeFromFavorite(user, context)

}