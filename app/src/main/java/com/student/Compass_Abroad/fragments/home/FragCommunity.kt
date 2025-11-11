package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterCommunityAds
import com.student.Compass_Abroad.adaptor.AdapterCommunityTabs
import com.student.Compass_Abroad.databinding.FragmentFragCommunityBinding
import com.student.Compass_Abroad.fragments.BaseFragment

class FragCommunity : BaseFragment() {
    private var binding: FragmentFragCommunityBinding? = null
    private lateinit var adapterCommunityAds: AdapterCommunityAds
    private lateinit var adapterCommunityTabs: AdapterCommunityTabs
    private lateinit var handlerAdds: Handler
    private lateinit var runnableAdds: Runnable

    private val sharedViewModel: SharedViewModel by activityViewModels()


    var addsImages: IntArray = intArrayOf(R.drawable.z5, R.drawable.z6)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragCommunityBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.navigationBarColor =  requireActivity().getColor(R.color.appSecondaryColor)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            setupAdsViewPager()
           // setupTabs()
            setViewPager()
            setupAutoScrollAds()
        }

        binding!!.fabFcNotification.setOnClickListener { v:View->

            Navigation.findNavController(v).navigate(R.id.studentInfoFragment)
        }

    }

    private fun FragmentFragCommunityBinding.setupAdsViewPager() {
        adapterCommunityAds = AdapterCommunityAds(requireActivity(), addsImages)
        viewPagerFcAf.adapter = adapterCommunityAds
    }

    private fun setViewPager() {
        val walletHVAdapter = AdapterCommunityTabs(childFragmentManager)
        binding?.vpFc?.adapter = walletHVAdapter
        binding?.tlFc?.setupWithViewPager(binding!!.vpFc)

        binding?.vpFc?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.d("ViewPager", "Page scrolled to position: $position with offset: $positionOffset")
            }

            override fun onPageSelected(position: Int) {
                Log.d("ViewPager", "Page selected: $position")
                sharedViewModel.triggerRefresh()
            }

            override fun onPageScrollStateChanged(state: Int) {

                when (state) {
                    ViewPager.SCROLL_STATE_IDLE -> Log.d("ViewPager", "Scrolling stopped")
                    ViewPager.SCROLL_STATE_DRAGGING -> Log.d("ViewPager", "Scrolling started")
                    ViewPager.SCROLL_STATE_SETTLING -> Log.d("ViewPager", "Scrolling settling")
                }
            }
        })
    }


    private fun FragmentFragCommunityBinding.setupAutoScrollAds() {
        handlerAdds = Handler()
        runnableAdds = object : Runnable {
            override fun run() {
                viewPagerFcAf.let {
                    val currentItem = it.currentItem
                    it.setCurrentItem((currentItem + 1) % addsImages.size, true)
                }
                handlerAdds.postDelayed(this, 3000) // Change delay as needed
            }
        }
        handlerAdds.post(runnableAdds)
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = true

    }

}