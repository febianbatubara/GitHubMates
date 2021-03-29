package com.febian.android.consumerapp.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febian.android.consumerapp.R
import com.febian.android.consumerapp.database.FavoriteUserDatabaseContract
import com.febian.android.consumerapp.databinding.ItemListUsersBinding

class ListUserAdapter(private val context: Context) :
    RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private var cursor: Cursor? = null

    fun setData(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
        val binding =
            ItemListUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = if (cursor == null) 0 else cursor!!.count

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        cursor?.let { bindView(holder, it.moveToPosition(position)) }
    }

    private fun bindView(holder: UserViewHolder, position: Boolean) {
        if (position) {
            with(holder) {
                binding.tvUsername.text = cursor?.getString(
                    cursor!!.getColumnIndexOrThrow(
                        FavoriteUserDatabaseContract.FavouriteUsersColumns.USER_USERNAME
                    )
                )

                val url = cursor?.getString(
                    cursor!!.getColumnIndexOrThrow(
                        FavoriteUserDatabaseContract.FavouriteUsersColumns.USER_AVATAR_URL
                    )
                )
                Glide.with(context)
                    .load(url)
                    .apply(RequestOptions().override(85, 85))
                    .placeholder(R.color.colorPrimary)
                    .into(binding.ivAvatar)
            }
        }
    }

    inner class UserViewHolder(val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

}