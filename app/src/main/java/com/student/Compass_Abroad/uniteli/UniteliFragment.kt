package com.student.Compass_Abroad.uniteli

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.FragmentUniteliBinding
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.uniteli.adapter.AdapterUniteliTabs

class UniteliFragment : Fragment() {
    private lateinit var binding: FragmentUniteliBinding
    private val TAG = "UniteliFragment"
    private var isFirstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUniteliBinding.inflate(inflater, container, false)

        setViewPager()

        onClicks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
    private fun onClicks() {
        binding.civFc.setOnClickListener {
            MainActivity.drawer?.open()
        }

        binding.fabFpNotificationStu.setOnClickListener {

            binding.root.findNavController().navigate(R.id.fragmentNotification)

        }
    }

    private fun setViewPager() {
        val titles = listOf(
            getString(R.string.tab_my_ambassadors),
            getString(R.string.tab_find_ambassadors)
        )

        val uniteliAdapter = AdapterUniteliTabs(childFragmentManager, titles)
        binding.vpFc.adapter = uniteliAdapter
        binding.tlFc.setupWithViewPager(binding.vpFc)

        binding.vpFc.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                Log.d(TAG, "Page selected: $position")
                refreshFragmentAtPosition(position)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun refreshFragmentAtPosition(position: Int) {
        // Use a safer way to find the fragment
        try {
            val fragmentTag = "android:switcher:${binding.vpFc.id}:$position"
            val fragment = childFragmentManager.findFragmentByTag(fragmentTag)

            if (fragment is MyAmbassadorChatFragment) {
                Log.d(TAG, "Found MyAmbassadorChatFragment at position $position, refreshing...")
                // Make sure view is created before trying to refresh
                if (fragment.isAdded && fragment.view != null) {
                    // Clear chat list first
                    fragment.clearChatList()
                    // Then force refresh data
                    fragment.forceApiRefresh()
                } else {
                    // If fragment view isn't ready yet, try again after a delay
                    Log.d(TAG, "Fragment view not ready, scheduling refresh later")
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (fragment.isAdded && fragment.view != null) {
                            fragment.clearChatList()
                            fragment.forceApiRefresh()
                        }
                    }, 200)
                }
            } else {
                Log.d(TAG, "Fragment at position $position is not MyAmbassadorChatFragment or is null")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing fragment at position $position", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
        MainActivity.bottomNav?.visibility = View.VISIBLE

        // Load user image
        val imageUrl = App.sharedPre?.getString(AppConstants.USER_IMAGE, "")?.trim('"') ?: ""
//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding.civFc)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, // light icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Refresh current fragment if it's not the first load (to avoid double refresh)
        if (!isFirstLoad) {
            Handler(Looper.getMainLooper()).postDelayed({
                Log.d(TAG, "onResume: Refreshing current fragment")
                refreshFragmentAtPosition(binding.vpFc.currentItem)
            }, 300)
        } else {
            isFirstLoad = false
        }
    }
}