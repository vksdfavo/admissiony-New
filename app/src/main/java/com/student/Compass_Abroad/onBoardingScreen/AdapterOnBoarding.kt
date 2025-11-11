package com.student.Compass_Abroad.onBoardingScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterOnBoarding(
    fragmentManager: FragmentManager?,
    lifecycle: Lifecycle?,
    var totalTabs: Int
    ) : FragmentStateAdapter(
    fragmentManager!!, lifecycle!!
    ) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {

                Page2()
            }

            2 -> {

                Page4()
            }

            else -> {

                Page1()

            }
        } as Fragment
    }

    override fun getItemCount(): Int {
        return totalTabs
    }
}
