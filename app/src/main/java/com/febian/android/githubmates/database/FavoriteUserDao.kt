package com.febian.android.githubmates.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.febian.android.githubmates.model.User

@Dao
interface FavoriteUserDao {

    @Query("SELECT * FROM user")
    fun getFavoriteUsers(): Cursor

    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getFavoriteUserById(id: Int): Cursor?

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id=(:id))")
    fun checkFavoriteUser(id: Int): Boolean

    @Insert
    fun addToFavorite(user: User): Long

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteFromFavorite(id: Int): Int

}