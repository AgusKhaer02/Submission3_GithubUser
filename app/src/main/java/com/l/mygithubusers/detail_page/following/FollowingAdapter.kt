package com.l.mygithubusers.detail_page.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.mygithubusers.databinding.ListFollowingBinding

class FollowingAdapter(private val listFollowing: ArrayList<FollowingDataModule>): RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = listFollowing.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

    inner class ViewHolder(private val binding: ListFollowingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModule: FollowingDataModule) {
            with(binding){
                Glide.with(itemView.context)
                    .load(dataModule.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(imgProfile)

                txtUsernameFollowing.text = dataModule.username
            }
        }
    }
}