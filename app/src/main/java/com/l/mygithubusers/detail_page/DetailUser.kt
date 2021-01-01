package com.l.mygithubusers.detail_page

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.mygithubusers.R
import com.l.mygithubusers.databinding.ActivityDetailUserBinding
import org.json.JSONObject

class DetailUser : AppCompatActivity() {

    //  VIEW BINDING
    private lateinit var binding : ActivityDetailUserBinding

    companion object {
        const val EXTRA_USERNAME = "ex_username"
        const val EXTRA_AVATAR = "ex_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      Activity Main Layout
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//      #Activity Main Layout------------------

        val username = getStringExtra(EXTRA_USERNAME)
        val avatar = getStringExtra(EXTRA_AVATAR)

        binding.email.text = ""
        binding.company.text = ""
        binding.location.text = ""
        getDetails(username)

//      Support Fragment Manager
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager,username)

//      View Pager
        binding.viewPager.adapter = sectionsPagerAdapter

//      Tab Layout
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.username.text = username
        Glide.with(this)
            .load(avatar)
            .apply(RequestOptions().override(100, 100))
            .into(binding.avatar)
    }

    private fun getDetails(user : String){
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/users/$user")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {
                    showLoading(false)
                    val company = response!!.getString("company") ?: applicationContext.resources.getString(
                        R.string.not_found)
                    val location = response.getString("location") ?: applicationContext.resources.getString(
                        R.string.not_found)
                    val email = response.getString("email") ?: applicationContext.resources.getString(
                        R.string.not_found)

                    binding.email.text = email
                    binding.location.text = location
                    binding.company.text = company

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
                            Toast.makeText(this@DetailUser, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    anError?.printStackTrace()
                }

            })
    }

    private fun getStringExtra(extras : String) : String{
        return intent.getStringExtra(extras) ?: ""
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}