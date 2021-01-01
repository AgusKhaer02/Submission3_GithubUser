package com.l.mygithubusers.detail_page.followers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.mygithubusers.databinding.ListFollowersBinding

class FollowersAdapter(private val listFollowers: ArrayList<FollowersDataModule>): RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = listFollowers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFollowers[position])
    }

    inner class ViewHolder(private val binding: ListFollowersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModule: FollowersDataModule) {
            with(binding){
                Glide.with(itemView.context)
                    .load(dataModule.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(imgProfile)

                txtUsernameFollowers.text = dataModule.username
            }
        }
    }
}