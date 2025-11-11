package com.student.Compass_Abroad.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.student.Compass_Abroad.fragments.home.FragApplicationAssignedStaff
import com.student.Compass_Abroad.fragments.home.FragApplicationTimeline
import com.student.Compass_Abroad.fragments.home.FragmentApplicationDocument
import com.student.Compass_Abroad.fragments.home.FragmentNotes
import com.student.Compass_Abroad.fragments.home.FragmentPayments
import com.student.Compass_Abroad.fragments.home.FragmentPrograms

class AdapterApplicationDetailTabs(
    fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?,
    var mTotalTabs: Int,
) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            4 -> {
                FragmentPayments()
            }

            3 -> {
                FragmentApplicationDocument()
            }

            2 -> {
                FragmentPrograms()

            }

            1 -> {
                FragApplicationAssignedStaff()
            }

            else -> {
                FragApplicationTimeline()
            }
        } as Fragment
    }

    override fun getItemCount(): Int {
        return mTotalTabs
    }
}