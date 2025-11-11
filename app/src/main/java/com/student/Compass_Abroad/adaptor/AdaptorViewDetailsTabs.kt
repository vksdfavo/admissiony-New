package com.student.Compass_Abroad.adaptor


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.SubAdditionalInfo
import com.student.Compass_Abroad.fragments.home.SubCheckList
import com.student.Compass_Abroad.fragments.home.SubProgramDetails
import com.student.Compass_Abroad.fragments.home.SubRequirements

class AdaptorViewDetailsTabs(fragmentManager: FragmentManager?, lifecycle: Lifecycle?,
                             var mTotalTabs: Int
) :
FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    override fun createFragment(position: Int): Fragment {

        return when (position) {
       3-> SubCheckList()
            2 -> SubAdditionalInfo()
            1 -> SubRequirements()
            else -> SubProgramDetails()
        }


    }

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}