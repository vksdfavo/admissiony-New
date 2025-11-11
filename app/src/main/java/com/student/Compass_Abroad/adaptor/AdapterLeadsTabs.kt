package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.FragmentAgentChat
import com.student.Compass_Abroad.fragments.home.FragmentInternalChat
import com.student.Compass_Abroad.fragments.home.FragmentVendorChat

class AdapterLeadsTabs(fragmentManager: FragmentManager?, lifecycle: Lifecycle?,
                       private var mTotalTabs: Int) : FragmentStateAdapter(
fragmentManager!!, lifecycle!!) { override fun createFragment(position: Int): Fragment {
    return if(position==2){
        FragmentVendorChat()
    }
    else if (position == 1) {
        FragmentInternalChat()
    } else {
        FragmentAgentChat()
    }
}

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}