package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.FragmentAgentChat
import com.student.Compass_Abroad.fragments.home.FragmentInternalChat
import com.student.Compass_Abroad.fragments.home.FragmentVendorChat

class AdapterChatsTabs(
    fragmentManager: Fragment?,
    private var mTotalTabs: Int
) : FragmentStateAdapter(
    fragmentManager!!
) {

    val list = arrayListOf(FragmentAgentChat(), FragmentInternalChat(), FragmentVendorChat())


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            2 -> {
                list[2]

            }

            1 -> {

                list[1]
            }

            else -> {
                list[0]
            }
        }
    }

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}