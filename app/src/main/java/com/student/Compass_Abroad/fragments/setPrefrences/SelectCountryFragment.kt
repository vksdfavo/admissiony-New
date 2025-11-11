package com.student.Compass_Abroad.fragments.setPrefrences

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.SavePreferencesRequest
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.setPrefrences.CountryAdaptor
import com.student.Compass_Abroad.databinding.FragmentSelectCountryBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.preferCountryList.Data
import com.student.Compass_Abroad.modal.savePeferences.SavePreferences
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONArray

class SelectCountryFragment : BaseFragment() {

    private lateinit var binding: FragmentSelectCountryBinding
    private var countryAdapter: CountryAdaptor? = null
    private var isSelectCountry: Boolean = false
    private var selectedDisciplinesJson: String? = ""
    private var prefferedCountriesList: ArrayList<Data> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCountryBinding.inflate(inflater, container, false)
        setPreferredCountriesAdapter()

        binding.view1.setBackgroundResource(R.color.secondary_color)       // green
        binding.view2.setBackgroundResource(R.color.bottom_nav_grey)       // grey
        binding.view3.setBackgroundResource(R.color.bottom_nav_grey)       // grey

        sharedPre?.getString(AppConstants.USER_PREFERENCES, "")


        binding.fabAcBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

       /* binding.tvSkip.setOnClickListener {

            binding.root.findNavController().navigate(R.id.disciplineFragment)

        }*/

        binding.tvNext2.setOnClickListener {

          selectCountry()
        }

        return binding.root
    }

    private fun selectCountry() {
        val selectedCountry = countryAdapter?.getSelectedCountry()

        if (selectedCountry != null) {

            val selectedValue = selectedCountry.value


            App.singleton!!.selectedCountry = selectedValue

            sharedPre?.saveString(AppConstants.USER_PREFERENCES,selectedValue)


            binding.root.findNavController().navigate(R.id.disciplineFragment)


        } else {
            CommonUtils.toast(requireContext(), "Please select a country")
        }
    }





    private fun setPreferredCountriesAdapter() {
        ViewModalClass().getCountryListProgramList(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->
            if (response != null && response.success) {
                val countryList = response.data

                prefferedCountriesList.clear()
                if (countryList != null) {
                    prefferedCountriesList.addAll(countryList)
                }

                countryAdapter = CountryAdaptor(
                    requireActivity(),
                    prefferedCountriesList,
                    object : CountryAdaptor.Select {
                        override fun click(selectCountry: Data?) {
                            isSelectCountry = true
                        }
                    }
                )

                // Get saved preference
                val savedValue = App.sharedPre?.getString(AppConstants.USER_PREFERENCES, "")

                // Preselect if match found
                val selectedIndex = prefferedCountriesList.indexOfFirst { it.value == savedValue }
                if (selectedIndex != -1) {
                    countryAdapter?.selectedItemPosition = selectedIndex
                    isSelectCountry = true
                }

                binding.recylcerview.setHasFixedSize(true)
                binding.recylcerview.adapter = countryAdapter

                // Notify to update UI
                countryAdapter?.notifyDataSetChanged()
            } else {
                // Handle API error
            }
        }
    }

    // Optional: Access selected country
    private fun getSelectedCountry(): Data? {
        return countryAdapter?.getSelectedCountry()
    }
}
