package com.student.Compass_Abroad.fragments.counselling

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterCounsellingTabs
import com.student.Compass_Abroad.databinding.FragmentCounsellingBinding

@Suppress("DEPRECATION")
class CounsellingFragment : Fragment() {
    private var _binding: FragmentCounsellingBinding? = null
    private val binding get() = _binding!!

    private var counsellingAdapter: AdapterCounsellingTabs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CounsellingFragment", "📱 onCreateView")

        _binding = FragmentCounsellingBinding.inflate(inflater, container, false)

        setupClickListeners()
        setupWindowInsets()
        setViewPager()

        requireActivity().window.navigationBarColor =
            requireActivity().getColor(R.color.theme_color)

        return binding.root
    }

    private fun setupClickListeners() {
        binding.civFc.setOnClickListener {
            MainActivity.drawer!!.open()
        }

        binding.fabFpNotificationStu.setOnClickListener {
            binding.root.findNavController().navigate(R.id.fragmentNotification)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setViewPager() {
        val titles = listOf(
            getString(R.string.status_scheduled),
            getString(R.string.status_completed)
        )

        counsellingAdapter = AdapterCounsellingTabs(childFragmentManager, titles)
        binding.vpFc.adapter = counsellingAdapter
        binding.tlFc.setupWithViewPager(binding.vpFc)

        // ✅ Keep both fragments alive to prevent black screen
        binding.vpFc.offscreenPageLimit = 2

        binding.vpFc.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Not needed
            }

            override fun onPageSelected(position: Int) {
                Log.d("CounsellingFragment", "📍 Page selected: $position")

                // ✅ Small delay to ensure fragment is fully visible
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        val fragment = counsellingAdapter?.getCachedFragment(position)
                        Log.d("CounsellingFragment", "Fragment type: ${fragment?.javaClass?.simpleName}")

                        when (fragment) {
                            is ScheduledFragment -> {
                                if (fragment.isResumed) {
                                    Log.d("CounsellingFragment", "✅ Refreshing ScheduledFragment")
                                    fragment.refreshData()
                                }
                            }
                            is CompletedFragment -> {
                                if (fragment.isResumed) {
                                    Log.d("CounsellingFragment", "✅ Refreshing CompletedFragment")
                                    // fragment.refreshData() // Add if CompletedFragment has this method
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CounsellingFragment", "Error refreshing fragment: ${e.message}", e)
                    }
                }, 250)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_IDLE -> Log.d("CounsellingFragment", "Scroll: IDLE")
                    ViewPager.SCROLL_STATE_DRAGGING -> Log.d("CounsellingFragment", "Scroll: DRAGGING")
                    ViewPager.SCROLL_STATE_SETTLING -> Log.d("CounsellingFragment", "Scroll: SETTLING")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("CounsellingFragment", "🟢 onResume")

        MainActivity.bottomNav!!.visibility = View.VISIBLE

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor == "admisiony") {
            requireActivity().window.navigationBarColor =
                requireActivity().getColor(R.color.bottom_gradient_one)
        } else {
            requireActivity().window.navigationBarColor =
                requireActivity().getColor(R.color.navigationBarColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CounsellingFragment", "🔴 onDestroyView")
        _binding = null
    }
}