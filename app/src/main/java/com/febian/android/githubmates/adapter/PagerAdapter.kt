package com.febian.android.githubmates.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.febian.android.githubmates.R
import com.febian.android.githubmates.activity.Application

class PagerAdapter(fm: FragmentManager, private val pages: List<Fragment>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> Application.appContext?.getString(R.string.followers)
            else -> Application.appContext?.getString(R.string.following)
        }
    }

}