@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.student.Compass_Abroad.fragments.StaffShortListFragment
import com.student.Compass_Abroad.fragments.counselling.CompletedFragment
import com.student.Compass_Abroad.fragments.counselling.ScheduledFragment
import com.student.Compass_Abroad.fragments.home.ShortListedFragment

class AdapterShorListedTabs(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ShortListedFragment()
            1 -> StaffShortListFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Self Shortlisted"
            1 -> "Assigned by Staff"
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
