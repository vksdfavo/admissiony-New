package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.FragProgramDetailDetails
import com.student.Compass_Abroad.fragments.home.FragProgramDetailRequirements


class AdapterProgramDetailTabs(
    fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?,
    var mTotalTabs: Int
) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            FragProgramDetailRequirements();
        } else {
            FragProgramDetailDetails();
        }
    }

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}