package com.febian.android.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

const val AUTHORITY = "com.febian.android.githubmates"
const val SCHEME = "content"

class FavoriteUserDatabaseContract {

    object FavouriteUsersColumns : BaseColumns {
        private const val DATABASE_NAME = "favorite_user_database"
        const val USER_USERNAME = "username"
        const val USER_AVATAR_URL = "avatarUrl"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(DATABASE_NAME)
            .build()
    }

}
