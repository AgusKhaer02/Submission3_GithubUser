package com.l.mygithubusers.detail_page

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.l.mygithubusers.detail_page.following.FollowingFragment
import com.l.mygithubusers.R
import com.l.mygithubusers.detail_page.followers.FollowersFragment
import com.l.mygithubusers.detail_page.repos.ReposFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager, private val username : String)
    : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.USERNAME_FOLLOWING, username)
                fragment.arguments = bundle
            }
            1 -> {
                fragment = FollowersFragment()
                val bundle = Bundle()
                bundle.putString(FollowersFragment.USERNAME_FOLLOWER, username)
                fragment.arguments = bundle
            }
            else -> {
                fragment = ReposFragment()
                val bundle = Bundle()
                bundle.putString(ReposFragment.USERNAME_REPOS, username)
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 3
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }



}