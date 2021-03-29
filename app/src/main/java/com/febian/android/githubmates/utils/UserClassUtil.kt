package com.febian.android.githubmates.utils

import android.content.ContentValues
import android.database.Cursor
import com.febian.android.githubmates.model.User
import java.util.*

object UserClassUtil {

    private const val USER_ID = "id"
    private const val USER_USERNAME = "username"
    private const val USER_NAME = "name"
    private const val USER_AVATAR_URL = "avatarUrl"
    private const val USER_COMPANY = "company"
    private const val USER_LOCATION = "location"
    private const val USER_REPOSITORIES = "repositoryCount"
    private const val USER_FOLLOWERS = "followers"
    private const val USER_FOLLOWING = "following"
    private const val USER_PROFILE_URL = "profileUrl"
    private const val USER_FOLLOWERS_URL = "followersUrl"
    private const val USER_FOLLOWING_URL = "followingUrl"
    private const val USER_CREATED_DATE = "createdDate"

    fun Cursor.toUser(): User = User(
        id = getInt(getColumnIndexOrThrow(USER_ID)),
        username = getString(getColumnIndexOrThrow(USER_USERNAME)),
        name = getString(getColumnIndexOrThrow(USER_NAME)),
        avatarUrl = getString(getColumnIndexOrThrow(USER_AVATAR_URL)),
        company = getString(getColumnIndexOrThrow(USER_COMPANY)),
        location = getString(getColumnIndexOrThrow(USER_LOCATION)),
        repositoryCount = getInt(getColumnIndexOrThrow(USER_REPOSITORIES)),
        followers = getInt(getColumnIndexOrThrow(USER_FOLLOWERS)),
        following = getInt(getColumnIndexOrThrow(USER_FOLLOWING)),
        profileUrl = getString(getColumnIndexOrThrow(USER_PROFILE_URL)),
        followersUrl = getString(getColumnIndexOrThrow(USER_FOLLOWERS_URL)),
        followingUrl = getString(getColumnIndexOrThrow(USER_FOLLOWING_URL)),
        createdDate = getString(getColumnIndexOrThrow(USER_CREATED_DATE)),
    )

    fun cursorToListUser(cursor: Cursor): ArrayList<User> {
        val userList = ArrayList<User>()
        cursor.apply {
            while (moveToNext()) {
                userList.add(
                    toUser()
                )
            }
        }
        return userList
    }

    fun contentValuesToUser(contentValues: ContentValues): User {
        return User(
            id = contentValues.getAsInteger(USER_ID),
            username = contentValues.getAsString(USER_USERNAME),
            name = contentValues.getAsString(USER_NAME),
            avatarUrl = contentValues.getAsString(USER_AVATAR_URL),
            company = contentValues.getAsString(USER_COMPANY),
            location = contentValues.getAsString(USER_LOCATION),
            repositoryCount = contentValues.getAsInteger(USER_REPOSITORIES),
            followers = contentValues.getAsInteger(USER_FOLLOWERS),
            following = contentValues.getAsInteger(USER_FOLLOWING),
            profileUrl = contentValues.getAsString(USER_PROFILE_URL),
            followersUrl = contentValues.getAsString(USER_FOLLOWERS_URL),
            followingUrl = contentValues.getAsString(USER_FOLLOWING_URL),
            createdDate = contentValues.getAsString(USER_CREATED_DATE)
        )
    }

    fun userToContentValues(user: User): ContentValues {
        return ContentValues().apply {
            with(user) {
                put(USER_ID, id)
                put(USER_USERNAME, username)
                put(USER_NAME, name)
                put(USER_AVATAR_URL, avatarUrl)
                put(USER_COMPANY, company)
                put(USER_LOCATION, location)
                put(USER_REPOSITORIES, repositoryCount)
                put(USER_FOLLOWERS, followers)
                put(USER_FOLLOWING, following)
                put(USER_PROFILE_URL, profileUrl)
                put(USER_FOLLOWERS_URL, followersUrl)
                put(USER_FOLLOWING_URL, followingUrl)
                put(USER_CREATED_DATE, createdDate)
            }
        }
    }

}

