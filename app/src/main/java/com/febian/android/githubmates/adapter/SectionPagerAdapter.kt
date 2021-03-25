package com.febian.android.githubmates.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.febian.android.githubmates.fragment.FollowersFragment
import com.febian.android.githubmates.fragment.FollowingFragment

internal class SectionsPagerAdapter(
    fa: FragmentActivity,
    private val bundle: Bundle
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment?
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                fragment.arguments = bundle
            }
            else -> {
                fragment = FollowingFragment()
                fragment.arguments = bundle
            }
        }
        return fragment
    }
}