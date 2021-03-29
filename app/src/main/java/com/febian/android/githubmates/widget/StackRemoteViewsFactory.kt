package com.febian.android.githubmates.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febian.android.githubmates.R
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.repository.FavoriteUserRepository.Companion.CONTENT_URI
import com.febian.android.githubmates.utils.UserClassUtil.cursorToListUser

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var userWidgetItems: List<User> = listOf()
    private var cursor: Cursor? = null

    override fun onCreate() {}

    override fun onDataSetChanged() {
        cursor?.close()

        val identityToken = Binder.clearCallingIdentity()

        cursor = context.contentResolver?.query(
            Uri.parse("$CONTENT_URI"),
            null,
            null,
            null,
            null
        )
        cursor?.let {
            userWidgetItems = cursorToListUser(it)
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        cursor?.close()
        userWidgetItems = listOf()
    }

    override fun getCount(): Int {
        return userWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val itemUser: User = userWidgetItems[position]
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        try {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(itemUser.avatarUrl)
                .apply(RequestOptions().fitCenter())
                .submit(800, 550)
                .get()
            rv.setImageViewBitmap(R.id.iv_avatar, bitmap)
            rv.setTextViewText(R.id.tv_username, itemUser.username)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val extras = Bundle()
        extras.putInt(FavoriteUserWidget.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.iv_avatar, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}