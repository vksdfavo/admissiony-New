@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.student.Compass_Abroad.fragments.home.FragCommunityAllFeeds
import com.student.Compass_Abroad.fragments.home.FragCommunityFollowing

class AdapterCommunityTabs(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragCommunityAllFeeds()
            1 -> FragCommunityFollowing()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "All Posts"
            1 -> "My Post"
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
