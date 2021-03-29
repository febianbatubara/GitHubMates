package com.febian.android.githubmates.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.febian.android.githubmates.database.FavoriteUserDatabase
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.utils.UserClassUtil.cursorToListUser
import com.febian.android.githubmates.utils.UserClassUtil.toUser
import com.febian.android.githubmates.utils.UserClassUtil.userToContentValues

class FavoriteUserRepository private constructor(context: Context) {

    companion object {
        const val DATABASE_NAME = "favorite_user_database"
        private var INSTANCE: FavoriteUserRepository? = null

        const val AUTHORITY = "com.febian.android.githubmates"
        private const val SCHEME = "content"

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FavoriteUserRepository(context)
            }
        }

        fun get(): FavoriteUserRepository {
            return INSTANCE
                ?: throw IllegalStateException("FavoriteUserRepository must be initialized")
        }

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(DATABASE_NAME)
            .build()
    }

    private val db: FavoriteUserDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FavoriteUserDatabase::class.java,
            DATABASE_NAME
        ).build()

    private val favoriteUserDao = db.userDao()

    fun addToFavorite(user: User) = favoriteUserDao.addToFavorite(user)

    fun removeFromFavorite(id: Int) = favoriteUserDao.deleteFromFavorite(id)

    fun getFavoriteUsersCursor() = favoriteUserDao.getFavoriteUsers()

    fun getFavoriteUserCursorById(id: Int) = favoriteUserDao.getFavoriteUserById(id)

    fun getFavoriteUsers(context: Context): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        liveData.postValue(cursor?.let { cursorToListUser(it) })
        cursor?.close()

        return liveData
    }

    fun addToFavorite(user: User, context: Context): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        val cursor = context.contentResolver.insert(CONTENT_URI, userToContentValues(user))
        cursor?.let { liveData.postValue(1) }

        return liveData
    }

    fun removeFromFavorite(user: User, context: Context): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        val uri = Uri.parse("$CONTENT_URI/${user.id}")
        val cursor = context.contentResolver.delete(uri, null, null)
        cursor.let { liveData.postValue(it.toLong()) }

        return liveData
    }

    fun checkFavoriteUser(id: Int, context: Context): LiveData<User> {
        val liveData = MutableLiveData<User>()
        val uri = Uri.parse("$CONTENT_URI/${id}")
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (cursor.moveToFirst()) {
                liveData.postValue(cursor.toUser())
            } else {
                liveData.postValue(null)
            }
            cursor.close()
        }
        return liveData
    }

}