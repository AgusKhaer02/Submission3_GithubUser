package com.l.mygithubusers.detail_page.followers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.l.mygithubusers.databinding.FragmentFollowersBinding
import org.json.JSONArray

class FollowersFragment : Fragment() {

    lateinit var followersAdapter: FollowersAdapter
    var followersArrayList = ArrayList<FollowersDataModule>()
    private var fragmentFollowers: FragmentFollowersBinding? = null
    private val binding get() = fragmentFollowers!!

    companion object {
        const val USERNAME_FOLLOWER = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowers = FragmentFollowersBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // jika arguments tidak null maka ambil data username dan panggil fungsi showFollower
        arguments?.let { it ->
            val username = it.getString(USERNAME_FOLLOWER, "null")

            getFollowers(username)
        }
    }


    private fun getFollowers(username : String) {
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/users/$username/followers")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    showLoading(false)
                    followersArrayList.clear()

                    response.let {
                        Log.d("json-length", it!!.length().toString())
                    }

                    val resLength = response!!.length()
                    if (resLength == 0){

                        Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show()
                    }else{
                        for (i in 0 until resLength){

                            val jsonObject = response.getJSONObject(i)

                            followersArrayList.add(
                                FollowersDataModule(
//                                  proses pengembilan data dari json ke DataModule
                                    jsonObject.getString("avatar_url"),
                                    jsonObject.getString("login")
                                )
                            )
                        }

                        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvFollowers.setHasFixedSize(true)
                        binding.rvFollowers.layoutManager = layoutManager

                        followersAdapter = FollowersAdapter(followersArrayList)
                        followersAdapter.notifyDataSetChanged()
                        binding.rvFollowers.adapter = followersAdapter
                    }

                }

                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                override fun onError(anError: ANError?) {
                    showLoading(false)
//                  error dan respon data tidak muncul
                    Log.d("msg-onerror",anError?.errorDetail?.toString())
                    Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    anError?.printStackTrace()
                }

            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFollowers = null
    }
}