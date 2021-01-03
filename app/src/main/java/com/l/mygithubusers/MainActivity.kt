package com.l.mygithubusers

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.l.mygithubusers.databinding.ActivityMainBinding
import com.l.mygithubusers.detail_page.DetailUser
import com.l.mygithubusers.users.UserAdapter
import com.l.mygithubusers.users.UserDataModule
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

//  VIEW BINDING
    private lateinit var binding : ActivityMainBinding

    private lateinit var searchView : SearchView
    private lateinit var searchManager : SearchManager

    lateinit var userAdapter: UserAdapter
    var userArrayList = ArrayList<UserDataModule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      Activity Main Layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//      #Activity Main Layout------------------
        getUsers()
    }


//  OPTIONS MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        //  search input
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.search)!!.actionView as SearchView
        val searchItem = menu.findItem(R.id.search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                findUsers(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }


        })

        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                getUsers()
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUsers() : Boolean {
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/users")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {

                    userArrayList.clear()

                    if (response?.length() == 0){

                        Toast.makeText(this@MainActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                    }else{

                        for (i in 0 until response?.length()!!){

                            val jsonObject = response.getJSONObject(i)

                            userArrayList.add(
                                UserDataModule(
//                                  proses pengembilan data dari json ke DataModule
                                    jsonObject.getString("login"),
                                    jsonObject.getString("avatar_url")
                                )
                            )
                        }
                        userAdapter = UserAdapter(userArrayList)
                        userAdapter.notifyDataSetChanged()
                        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvUsersList.setHasFixedSize(true)
                        binding.rvUsersList.layoutManager = layoutManager
                        binding.rvUsersList.adapter = userAdapter

                        showLoading(false)
                        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
                            override fun onItemClicked(data: UserDataModule) {
                                val intent = Intent(this@MainActivity, DetailUser::class.java)
                                intent.putExtra(DetailUser.EXTRA_USERNAME,data.Username)
                                intent.putExtra(DetailUser.EXTRA_AVATAR,data.Avatar)
                                startActivity(intent)
                            }
                        })

                    }

                }

                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                override fun onError(anError: ANError?) {
//                  error dan respon data tidak muncul
                    showLoading(false)
                    Log.d("msg-onerror",anError?.errorDetail?.toString())
                    Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    anError?.printStackTrace()
                }

            })
        return false
    }

    private fun findUsers(user : String){
        showLoading(true)
        AndroidNetworking.get("https://api.github.com/search/users?q=$user")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    userArrayList.clear()

                    if (response?.length() == 0){

                        Toast.makeText(this@MainActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                    }else{

                        val jsonArray = response?.getJSONArray("items")
                        for (i in 0 until jsonArray!!.length()){
                            val jsonObject = jsonArray.getJSONObject(i)
                            userArrayList.add(
                                UserDataModule(
//                                  proses pengembilan data dari json ke DataModule
                                    jsonObject.getString("login"),
                                    jsonObject.getString("avatar_url")
                                )
                            )
                        }

                        userAdapter = UserAdapter(userArrayList)
                        userAdapter.notifyDataSetChanged()
                        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvUsersList.setHasFixedSize(true)
                        binding.rvUsersList.layoutManager = layoutManager
                        binding.rvUsersList.adapter = userAdapter

                        showLoading(false)

                        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
                            override fun onItemClicked(data: UserDataModule) {
                                val intent = Intent(this@MainActivity, DetailUser::class.java)
                                intent.putExtra(DetailUser.EXTRA_USERNAME,data.Username)
                                intent.putExtra(DetailUser.EXTRA_AVATAR,data.Avatar)
                                startActivity(intent)
                            }
                        })
                    }

                }

                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                override fun onError(anError: ANError?) {
//                  error dan respon data tidak muncul
                    showLoading(false)
                    Log.d("msg-onerror",anError?.errorDetail?.toString())
                    when (user) {
                        "" -> {

                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    anError?.printStackTrace()
                }

            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvUsersList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvUsersList.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}