package com.student.Compass_Abroad.fragments.setPrefrences

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.setPrefrences.CountryAdaptor
import com.student.Compass_Abroad.databinding.FragmentSelectCountryBinding
import com.student.Compass_Abroad.modal.preferCountryList.Data
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONArray

class SelectCountryFragment : Fragment() {

    private lateinit var binding: FragmentSelectCountryBinding

    private var countryAdapter: CountryAdaptor? = null

    private var prefferedCountriesList:
            ArrayList<Data> = ArrayList()

    // ✅ Loading flag
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            FragmentSelectCountryBinding.inflate(
                inflater,
                container,
                false
            )

        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                0
            )

            insets
        }

        // ✅ Step UI
        binding.view1.setBackgroundResource(
            R.color.secondary_color
        )

        binding.view2.setBackgroundResource(
            R.color.bottom_nav_grey
        )

        binding.view3.setBackgroundResource(
            R.color.bottom_nav_grey
        )

        // ✅ Disable next button initially
        binding.tvNext2.isEnabled = false

        binding.fabAcBack.setOnClickListener {

            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }

        binding.tvNext2.setOnClickListener {

            // ✅ Prevent fast click
            if (!isDataLoaded) {

                CommonUtils.toast(
                    requireContext(),
                    "Please wait, loading countries..."
                )

                return@setOnClickListener
            }

            selectCountry()
        }

        // ✅ API call
        fetchPreferencesAndLoadCountries()

        return binding.root
    }

    // ✅ Step 1: Fetch preferences
    private fun fetchPreferencesAndLoadCountries() {

        LoginViewModal().getPreferencesDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { prefResponse ->

            val apiSelectedCountries =
                mutableListOf<String>()

            if (prefResponse?.statusCode == 200) {

                val destinationCountry =
                    prefResponse.data
                        ?.preferencesInfo
                        ?.destination_country

                val parsed = when (destinationCountry) {

                    is List<*> -> {
                        destinationCountry
                            .filterIsInstance<String>()
                            .map { it.trim() }
                    }

                    is String -> {
                        destinationCountry
                            .split(",")
                            .map { it.trim() }
                    }

                    else -> emptyList()
                }

                apiSelectedCountries.addAll(
                    parsed.filter { it.isNotEmpty() }
                )

                Log.d(
                    "API_Countries",
                    "From API: $apiSelectedCountries"
                )
            }

            setPreferredCountriesAdapter(
                apiSelectedCountries
            )
        }
    }

    // ✅ Step 2: Load country list
    private fun setPreferredCountriesAdapter(
        selectedCountries: List<String>
    ) {

        ViewModalClass().getCountryListProgramList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->

            if (response != null && response.success) {

                val countryList = response.data

                prefferedCountriesList.clear()

                if (countryList != null) {
                    prefferedCountriesList
                        .addAll(countryList)
                }

                countryAdapter = CountryAdaptor(
                    requireActivity(),
                    prefferedCountriesList,

                    object : CountryAdaptor.Select {

                        override fun click(
                            selectedCountries: List<Data>
                        ) {

                            // Optional
                        }
                    }
                )

                // ✅ Pre-select countries
                if (selectedCountries.isNotEmpty()) {

                    countryAdapter
                        ?.setSelectedByValues(
                            selectedCountries
                        )
                }

                binding.recylcerview
                    .setHasFixedSize(true)

                binding.recylcerview.adapter =
                    countryAdapter

                // ✅ Data loaded
                isDataLoaded = true

                // ✅ Enable next button
                binding.tvNext2.isEnabled = true
            }
        }
    }

    // ✅ Step 3: Save selected country
    private fun selectCountry() {

        val selected =
            countryAdapter?.getSelectedCountries()

        if (!selected.isNullOrEmpty()) {

            val selectedValue =
                selected.first().value

            App.singleton!!.selectedCountry =
                listOf(selectedValue)

            sharedPre?.saveString(
                AppConstants.USER_PREFERENCES,
                JSONArray(
                    listOf(selectedValue)
                ).toString()
            )

            binding.root
                .findNavController()
                .navigate(R.id.disciplineFragment)

        } else {

            CommonUtils.toast(
                requireContext(),
                "Please select a country"
            )
        }
    }

    override fun onResume() {

        super.onResume()

        val window =
            requireActivity().window

        window.statusBarColor =
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )

        window.navigationBarColor =
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.R
        ) {

            val controller =
                window.insetsController

            controller?.setSystemBarsAppearance(
                WindowInsetsController
                    .APPEARANCE_LIGHT_STATUS_BARS,

                WindowInsetsController
                    .APPEARANCE_LIGHT_STATUS_BARS
            )

        } else {

            @Suppress("DEPRECATION")

            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}