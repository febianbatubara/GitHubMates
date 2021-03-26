package com.febian.android.githubmates.database

import androidx.room.Dao
import androidx.room.Query
import com.febian.android.githubmates.model.User

@Dao
interface FavoriteUserDao {

    @Query("SELECT * FROM user")
    fun getFavoriteUsers(): List<User>

    @Query("SELECT * FROM user WHERE username=(:username)")
    fun getFavoriteUser(username: String): User

}