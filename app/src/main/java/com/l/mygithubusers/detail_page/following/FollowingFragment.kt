package com.l.mygithubusers.detail_page.following

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
import com.l.mygithubusers.databinding.FragmentFollowingBinding
import org.json.JSONArray

class FollowingFragment : Fragment() {

    lateinit var followingAdapter: FollowingAdapter
    var followingArrayList = ArrayList<FollowingDataModule>()
    private var fragmentFollowing: FragmentFollowingBinding? = null
    private val binding get() = fragmentFollowing!!

    companion object {
        const val USERNAME_FOLLOWING = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentFollowing = FragmentFollowingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // jika arguments tidak null maka ambil data username dan panggil fungsi showFollower
        arguments?.let { it ->
            val username = it.getString(USERNAME_FOLLOWING, "null")

            getFollowing(username)
        }
    }


    private fun getFollowing(username : String) {
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/users/$username/following")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {

                    followingArrayList.clear()

                    response.let {
                        Log.d("json-length", it!!.length().toString())
                    }

                    val resLength = response!!.length()
                    if (resLength == 0){

                        Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }else{
                        for (i in 0 until resLength){

                            val jsonObject = response.getJSONObject(i)

                            followingArrayList.add(
                                FollowingDataModule(
//                                  proses pengembilan data dari json ke DataModule
                                    jsonObject.getString("avatar_url"),
                                    jsonObject.getString("login")
                                )
                            )
                        }

                        followingAdapter = FollowingAdapter(followingArrayList)
                        followingAdapter.notifyDataSetChanged()
                        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvFollowing.setHasFixedSize(true)
                        binding.rvFollowing.layoutManager = layoutManager
                        binding.rvFollowing.adapter = followingAdapter

                        showLoading(false)
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
            binding.rvFollowing.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvFollowing.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFollowing = null
    }
}