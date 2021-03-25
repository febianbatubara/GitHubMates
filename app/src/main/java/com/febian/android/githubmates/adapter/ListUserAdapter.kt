package com.febian.android.githubmates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febian.android.githubmates.R
import com.febian.android.githubmates.databinding.ItemListUsersBinding
import com.febian.android.githubmates.model.User

class ListUserAdapter(private val context: Context) :
    RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private var listUser = listOf<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(newList: List<User>) {
        listUser = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
        val binding =
            ItemListUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = listUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userItem = listUser[position]

        with(holder) {
            binding.tvUsername.text = userItem.username

            Glide.with(context)
                .load(userItem.avatarUrl)
                .apply(RequestOptions().override(85, 85))
                .placeholder(R.color.colorPrimary)
                .into(binding.ivAvatar)

            binding.itemUser.setOnClickListener {
                onItemClickCallback?.onItemClicked(userItem)
            }
        }
    }

    inner class UserViewHolder(val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}