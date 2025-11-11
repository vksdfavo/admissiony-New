package com.student.Compass_Abroad.Scout.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.adaptor.AllScoutProgramAdapter
import com.student.Compass_Abroad.databinding.FragmentFragScoutAllProgramBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragScoutAllProgram : BaseFragment() {
    private lateinit var binding: FragmentFragScoutAllProgramBinding
    var allScoutProgramAdapter: AllScoutProgramAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentFragScoutAllProgramBinding.inflate(inflater,container,false)
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding!!.civProfileImageFd2.setOnClickListener {
            ScoutMainActivity.drawer!!.open()
        }
        binding!!.fabFpHeart.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.fragScoutShortlistedProgram)
        }

        fetchAllProgramAdapter(binding!!)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun fetchAllProgramAdapter(_binding: FragmentFragScoutAllProgramBinding) {
        allScoutProgramAdapter = AllScoutProgramAdapter()
        _binding.rvFpAp?.adapter = allScoutProgramAdapter
    }

    override fun onResume() {
        super.onResume()
        ScoutMainActivity.bottomNav!!.isVisible = true
        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor=="admisiony")
        {
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.bottom_gradient_two)

        }else{
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.theme_color)

        }
    }

}