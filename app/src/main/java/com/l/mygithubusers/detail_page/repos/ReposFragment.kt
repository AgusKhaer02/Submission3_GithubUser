package com.l.mygithubusers.detail_page.repos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.l.mygithubusers.R
import com.l.mygithubusers.databinding.FragmentFollowersBinding
import com.l.mygithubusers.databinding.FragmentReposBinding
import com.l.mygithubusers.detail_page.DetailUser
import com.l.mygithubusers.detail_page.following.FollowingAdapter
import com.l.mygithubusers.detail_page.following.FollowingDataModule
import com.l.mygithubusers.users.UserAdapter
import com.l.mygithubusers.users.UserDataModule
import org.json.JSONArray
import org.json.JSONObject

class ReposFragment : Fragment() {

    lateinit var reposAdapter: ReposAdapter
    var reposArrayList = ArrayList<ReposDataModule>()
    private var fragmentRepos: FragmentReposBinding? = null
    private val binding get() = fragmentRepos!!

    companion object {
        const val USERNAME_REPOS = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRepos = FragmentReposBinding.inflate(inflater, container,false)
        arguments?.let {
            Log.d("usernameRepos",it.getString(USERNAME_REPOS, "null"))
            getRepos(it.getString(USERNAME_REPOS, "null"))
        }
        return binding.root
    }

    private fun getRepos(user : String){
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/users/$user/repos")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {

                    reposArrayList.clear()

                    if (response?.length() == 0){

                        Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show()
                    }else{

                        for (i in 0 until response!!.length()){
                            val jsonObject = response.getJSONObject(i)
                            reposArrayList.add(
                                ReposDataModule(
//                                  proses pengembilan data dari json ke DataModule
                                    jsonObject.getString("name"),
                                    jsonObject.getString("created_at"),
                                    jsonObject.getString("updated_at"),
                                    jsonObject.getString("pushed_at"),
                                    jsonObject.getInt("forks"),
                                    jsonObject.getString("language"),
                                    jsonObject.getInt("watchers"),

                                )
                            )
                        }

                        reposAdapter = ReposAdapter(reposArrayList)
                        reposAdapter.notifyDataSetChanged()
                        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvRepos.setHasFixedSize(true)
                        binding.rvRepos.layoutManager = layoutManager
                        binding.rvRepos.adapter = reposAdapter


                        showLoading(false)
                    }

                }

                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                override fun onError(anError: ANError?) {
                    showLoading(false)
//                  error dan respon data tidak muncul
                    Log.d("msg-onerror",anError?.errorDetail?.toString())
                    when (user) {
                        "" -> {

                        }
                        else -> {
                            Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    anError?.printStackTrace()
                }

            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvRepos.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvRepos.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentRepos = null
    }



}