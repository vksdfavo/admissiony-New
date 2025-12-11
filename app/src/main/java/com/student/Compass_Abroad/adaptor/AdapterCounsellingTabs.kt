package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.student.Compass_Abroad.fragments.counselling.CompletedFragment
import com.student.Compass_Abroad.fragments.counselling.ScheduledFragment

class AdapterCounsellingTabs(
    fm: FragmentManager,
    private val titles: List<String>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // ✅ Create fragments ONCE and reuse them (prevents black screen)
    private val fragments = listOf(
        ScheduledFragment(),
        CompletedFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    // ✅ Method to get fragment instance for manual refresh
    fun getCachedFragment(position: Int): Fragment? {
        return fragments.getOrNull(position)
    }
}