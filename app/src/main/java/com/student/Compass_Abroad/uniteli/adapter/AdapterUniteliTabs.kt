package com.student.Compass_Abroad.uniteli.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.student.Compass_Abroad.uniteli.MyAmbassadorChatFragment
import com.student.Compass_Abroad.uniteli.FindAmbassadorChatFragment

class AdapterUniteliTabs(
    fm: FragmentManager,
    private val titles: List<String> // Pass titles from outside
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        MyAmbassadorChatFragment(),
        FindAmbassadorChatFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }
}
