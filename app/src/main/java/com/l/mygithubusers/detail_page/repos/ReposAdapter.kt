package com.l.mygithubusers.detail_page.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.mygithubusers.databinding.ListReposBinding

class ReposAdapter(private val listRepos: ArrayList<ReposDataModule>): RecyclerView.Adapter<ReposAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = listRepos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRepos[position])
    }

    inner class ViewHolder(private val binding: ListReposBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModule: ReposDataModule) {
            with(binding){
                binding.txtRepoName.text = dataModule.repoName
                binding.txtCreatedAt.text = dataModule.createdAt
                binding.txtUpdatedAt.text = dataModule.updatedAt
                binding.txtPushedAt.text = dataModule.pushedAt

                binding.txtFork.text = dataModule.fork.toString()
                binding.txtLanguage.text = dataModule.language
                binding.txtWatched.text = dataModule.watched.toString()
            }
        }
    }
}