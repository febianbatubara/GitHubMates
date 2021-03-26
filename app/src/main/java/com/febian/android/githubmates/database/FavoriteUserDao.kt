package com.febian.android.githubmates.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.febian.android.githubmates.model.User

@Dao
interface FavoriteUserDao {

    @Query("SELECT * FROM user")
    fun getFavoriteUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username=(:username)")
    fun getFavoriteUser(username: String): LiveData<User?>

    @Insert
    fun addToFavorite(user: User)

    @Delete
    fun deleteFromFavorite(user: User)

}