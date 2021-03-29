package com.febian.android.githubmates.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.febian.android.githubmates.repository.FavoriteUserRepository
import com.febian.android.githubmates.repository.FavoriteUserRepository.Companion.AUTHORITY
import com.febian.android.githubmates.repository.FavoriteUserRepository.Companion.CONTENT_URI
import com.febian.android.githubmates.repository.FavoriteUserRepository.Companion.DATABASE_NAME
import com.febian.android.githubmates.utils.UserClassUtil.contentValuesToUser
import kotlinx.coroutines.*

class GitHubMatesProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_USER = 1
        private const val FAVORITE_USER_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        lateinit var favoriteUserRepository: FavoriteUserRepository
    }

    init {
        uriMatcher.addURI(
            AUTHORITY,
            DATABASE_NAME,
            FAVORITE_USER
        )

        uriMatcher.addURI(
            AUTHORITY,
            "$DATABASE_NAME/#",
            FAVORITE_USER_ID
        )
    }

    override fun onCreate(): Boolean {
        context?.let { FavoriteUserRepository.initialize(it) }
        favoriteUserRepository = FavoriteUserRepository.get()
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return runBlocking { getCursor(uri) }
    }

    private suspend fun getCursor(uri: Uri): Cursor {
        lateinit var cursor: Cursor
        val process = GlobalScope.async {
            val dispatcher = this.coroutineContext
            CoroutineScope(dispatcher).launch {
                cursor = when (uriMatcher.match(uri)) {
                    FAVORITE_USER -> favoriteUserRepository.getFavoriteUsersCursor()
                    else -> uri.lastPathSegment?.let {
                        favoriteUserRepository.getFavoriteUserCursorById(
                            it.toInt()
                        )
                    }!!
                }
            }
        }
        process.await()

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long? = when (FAVORITE_USER) {
            uriMatcher.match(uri) -> values?.let {
                contentValuesToUser(
                    it
                )
            }?.let { favoriteUserRepository.addToFavorite(it) }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_USER_ID) {
            uriMatcher.match(uri) -> (uri.lastPathSegment?.toInt()?.let {
                favoriteUserRepository.removeFromFavorite(
                    it
                )
            } ?: 0)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}
