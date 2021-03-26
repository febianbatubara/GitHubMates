package com.febian.android.githubmates.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.febian.android.githubmates.model.User

@Database(entities = [User::class], version = 1)
abstract class FavoriteUserDatabase : RoomDatabase() {

    abstract fun userDao(): FavoriteUserDao

}