package com.robdir.githubusers.presentation.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robdir.githubusers.databinding.LayoutListItemUserBinding
import com.robdir.githubusers.domain.users.User
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UsersAdapter @Inject constructor(private val picasso: Picasso) :
    ListAdapter<User, UsersAdapter.ViewHolder>(UserComparator) {

    interface Callback {
        fun onUserSelected(username: String, avatarUrl: String)
    }
    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position]?.let(holder::bind)
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        @JvmStatic
        private val UserComparator = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
        }
    }

    inner class ViewHolder(private val userBinding: LayoutListItemUserBinding) :
        RecyclerView.ViewHolder(userBinding.root) {

        fun bind(user: User) {
            with(userBinding) {
                layoutUserListItem.setOnClickListener { callback?.onUserSelected(user.username, user.avatarUrl) }
                textViewUserName.text = user.username
                picasso.load(user.avatarUrl).into(imageViewThumbnail)
            }
        }
    }
}
