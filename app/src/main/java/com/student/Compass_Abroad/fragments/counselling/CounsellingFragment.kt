package com.student.Compass_Abroad.fragments.counselling

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterCounsellingTabs
import com.student.Compass_Abroad.databinding.FragmentCounsellingBinding
import com.student.Compass_Abroad.fragments.BaseFragment

@Suppress("DEPRECATION")
class CounsellingFragment : Fragment() {
    private lateinit var binding: FragmentCounsellingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounsellingBinding.inflate(inflater, container, false)

        binding.civFc.setOnClickListener {

            MainActivity.drawer!!.open()

        }

        setViewPager()

        binding.fabFpNotificationStu.setOnClickListener {

                binding.root.findNavController().navigate(R.id.fragmentNotification)

            }


        requireActivity().window.navigationBarColor =  requireActivity().getColor(R.color.theme_color)

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

    private fun setViewPager() {
        val titles = listOf(
            getString(R.string.status_scheduled),
            getString(R.string.status_completed)
        )

        val walletHVAdapter = AdapterCounsellingTabs(childFragmentManager, titles)
        binding.vpFc.adapter = walletHVAdapter
        binding.tlFc.setupWithViewPager(binding.vpFc)

        binding.vpFc.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                Log.d("ViewPager", "Page scrolled to position: $position with offset: $positionOffset")

            }

            override fun onPageSelected(position: Int) {

                Log.d("ViewPager", "Page selected: $position")

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


    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.visibility = View.VISIBLE

        val imageUrl = App.sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')

//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding.civFc)

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor=="admisiony")
        {
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.bottom_gradient_one)

        }else{
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.navigationBarColor)

        }

    }
}