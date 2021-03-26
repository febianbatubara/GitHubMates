package com.febian.android.githubmates.repository

import android.content.Context
import androidx.room.Room
import com.febian.android.githubmates.database.FavoriteUserDatabase
import com.febian.android.githubmates.model.User

private const val DATABASE_NAME = "favorite_user_database"

class FavoriteUserRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE: FavoriteUserRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FavoriteUserRepository(context)
            }
        }

        fun get(): FavoriteUserRepository {
            return INSTANCE
                ?: throw IllegalStateException("FavoriteUserRepository must be initialized")
        }
    }

    private val db: FavoriteUserDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FavoriteUserDatabase::class.java,
            DATABASE_NAME
        ).build()

    private val favoriteUserDao = db.userDao()

    fun getFavoriteUsers() = favoriteUserDao.getFavoriteUsers()

    fun checkFavoriteUser(username: String) = favoriteUserDao.checkFavoriteUser(username)

    fun addToFavorite(user: User) = favoriteUserDao.addToFavorite(user)

    fun removeFromFavorite(user: User) = favoriteUserDao.deleteFromFavorite(user)

}