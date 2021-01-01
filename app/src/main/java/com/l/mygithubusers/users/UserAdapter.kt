package com.l.mygithubusers.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.mygithubusers.databinding.ListUsersBinding

class UserAdapter(private val listUsers: ArrayList<UserDataModule>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: UserDataModule)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    inner class ViewHolder(private val binding: ListUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModule: UserDataModule) {
            with(binding){
                Glide.with(itemView.context)
                    .load(dataModule.Avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(imgUsers)

                txtUsers.text = dataModule.Username
                btnSee.setOnClickListener {
                    onItemClickCallback?.onItemClicked(dataModule)
                }
            }
        }
    }
}