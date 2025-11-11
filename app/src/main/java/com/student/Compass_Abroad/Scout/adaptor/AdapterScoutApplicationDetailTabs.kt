package com.student.Compass_Abroad.Scout.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.Scout.fragment.FragScoutApplicationAssignedStaff
import com.student.Compass_Abroad.Scout.fragment.FragScoutApplicationTimeline
import com.student.Compass_Abroad.Scout.fragment.FragmentScoutChat


class AdapterScoutApplicationDetailTabs(fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?,
    var mTotalTabs: Int,
    ) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
        override fun createFragment(position: Int): Fragment {
            return (if (position == 2){
                FragmentScoutChat()
            } else if (position == 1) {
                FragScoutApplicationAssignedStaff()
            } else {
                FragScoutApplicationTimeline()
            }) as Fragment
        }

        override fun getItemCount(): Int {
            return mTotalTabs
        }
    }