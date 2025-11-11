package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.ApplicationActiveFragment
import com.student.Compass_Abroad.fragments.home.ApplicationArchivedFragment


class AdapterApplicationsTabs(
    fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?,
    private var mTotalTabs: Int) : FragmentStateAdapter(
    fragmentManager!!, lifecycle!!) { override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            ApplicationArchivedFragment()
        } else {
            ApplicationActiveFragment()
        }
    }

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}
