package com.student.Compass_Abroad.fragments.setPrefrences

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.SavePreferencesRequest
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.StudyLevelAdapter
import com.student.Compass_Abroad.databinding.FragmentStudyLevelBinding
import com.student.Compass_Abroad.modal.studyLevelModel.Data
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass

class StudyLevelFragment : Fragment(),
    StudyLevelAdapter.SelectStudyLevel {

    private lateinit var binding: FragmentStudyLevelBinding

    private var adapterScheduledAdapter: StudyLevelAdapter? = null

    private val studyLevelList: MutableList<Data> =
        mutableListOf()

    private var selectedCountry: List<String>? =
        emptyList()

    private val viewModel: LoginViewModal by lazy {
        LoginViewModal()
    }

    // ✅ Loading flag
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStudyLevelBinding.inflate(
            inflater,
            container,
            false
        )

        binding.view1.setBackgroundResource(
            R.color.secondary_color
        )

        binding.view2.setBackgroundResource(
            R.color.secondary_color
        )

        binding.view3.setBackgroundResource(
            R.color.secondary_color
        )

        selectedCountry = App.singleton?.selectedCountry

        val selectedDisciplinesJson =
            arguments?.getString("request_json")

        val type =
            object : TypeToken<List<String>>() {}.type

        val selectedDisciplines: List<String> =
            Gson().fromJson(
                selectedDisciplinesJson,
                type
            )

        Log.d(
            "SelectedStudyLevelItem",
            Gson().toJson(selectedDisciplines)
        )

        setApplicationActiveRecyclerview()

        // ✅ Disable submit until API loaded
        binding.btnSubmit.isEnabled = false

        onClicks(selectedDisciplines)

        // ✅ Fetch preferences first
        fetchPreferencesAndLoadStudyLevels()

        return binding.root
    }

    // ✅ Step 1: Fetch preferences API
    private fun fetchPreferencesAndLoadStudyLevels() {

        LoginViewModal().getPreferencesDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { prefResponse ->

            var apiStudyLevel: String? = null

            if (prefResponse?.statusCode == 200) {

                val studyLevel =
                    prefResponse.data
                        ?.preferencesInfo
                        ?.preferred_study_level

                apiStudyLevel = when (studyLevel) {

                    is List<*> -> {
                        studyLevel
                            .filterIsInstance<String>()
                            .firstOrNull()
                            ?.trim()
                    }

                    is String -> {
                        studyLevel.trim()
                    }

                    else -> null
                }

                Log.d(
                    "API_StudyLevel",
                    "From API: $apiStudyLevel"
                )
            }

            val currentFlavor =
                BuildConfig.FLAVOR.lowercase()

            when (currentFlavor) {

                "mavenconsulting" -> {
                    fetchDataFromApi2(apiStudyLevel)
                }

                else -> {
                    fetchDataFromApi(apiStudyLevel)
                }
            }
        }
    }

    // ✅ Normal flavor
    private fun fetchDataFromApi(
        preSelectedLabel: String? = null
    ) {

        LoginViewModal().getStudyLevelModalLiveData3(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}",
        ).observe(viewLifecycleOwner) { response ->

            response?.let {

                if (it.statusCode == 200 && it.success) {

                    val programResponse =
                        it.data ?: emptyList()

                    studyLevelList.clear()
                    studyLevelList.addAll(programResponse)

                    adapterScheduledAdapter
                        ?.notifyDataSetChanged()

                    val labelToSelect =
                        preSelectedLabel
                            ?.takeIf { label ->
                                label.isNotEmpty()
                            }
                            ?: sharedPre?.getString(
                                AppConstants.STUDY_LEVEL,
                                ""
                            )

                    adapterScheduledAdapter
                        ?.setPreSelectedLabel(labelToSelect)

                    Log.d(
                        "PreSelected_StudyLevel",
                        "Label: $labelToSelect"
                    )

                    binding.llFaActiveNoApplications.visibility =
                        if (studyLevelList.isEmpty())
                            View.VISIBLE
                        else
                            View.GONE

                    binding.rvFaActive.visibility =
                        if (studyLevelList.isEmpty())
                            View.GONE
                        else
                            View.VISIBLE

                    // ✅ Data loaded
                    isDataLoaded = true

                    // ✅ Enable button
                    binding.btnSubmit.isEnabled = true
                }

            } ?: run {

                binding.llFaActiveNoApplications.visibility =
                    View.VISIBLE

                binding.rvFaActive.visibility =
                    View.GONE
            }

            binding.pbFaActive.visibility =
                View.GONE
        }
    }

    // ✅ Maven flavor
    private fun fetchDataFromApi2(
        preSelectedLabel: String? = null
    ) {

        viewModel.getStudyLevelMavenModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}",
        ).observe(viewLifecycleOwner) { response ->

            response?.let {

                if (it.statusCode == 200 && it.success) {

                    val programResponse =
                        it.data ?: emptyList()

                    studyLevelList.clear()
                    studyLevelList.addAll(programResponse)

                    adapterScheduledAdapter
                        ?.notifyDataSetChanged()

                    val labelToSelect =
                        preSelectedLabel
                            ?.takeIf { label ->
                                label.isNotEmpty()
                            }
                            ?: sharedPre?.getString(
                                AppConstants.STUDY_LEVEL,
                                ""
                            )

                    adapterScheduledAdapter
                        ?.setPreSelectedLabel(labelToSelect)

                    Log.d(
                        "PreSelected_StudyLevel",
                        "Label: $labelToSelect"
                    )

                    binding.llFaActiveNoApplications.visibility =
                        if (studyLevelList.isEmpty())
                            View.VISIBLE
                        else
                            View.GONE

                    binding.rvFaActive.visibility =
                        if (studyLevelList.isEmpty())
                            View.GONE
                        else
                            View.VISIBLE

                    // ✅ Data loaded
                    isDataLoaded = true

                    // ✅ Enable button
                    binding.btnSubmit.isEnabled = true
                }

            } ?: run {

                binding.llFaActiveNoApplications.visibility =
                    View.VISIBLE

                binding.rvFaActive.visibility =
                    View.GONE
            }

            binding.pbFaActive.visibility =
                View.GONE
        }
    }

    private fun onClicks(
        selectedDiscipline: List<String>
    ) {

        binding.fabAcBack.setOnClickListener {
            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {

            // ✅ Prevent fast click
            if (!isDataLoaded) {

                CommonUtils.toast(
                    requireContext(),
                    "Please wait, loading study levels..."
                )

                return@setOnClickListener
            }

            val selected =
                adapterScheduledAdapter
                    ?.getSelectedItem()

            Log.d(
                "SelectedStudyLevelItem",
                selected?.label.toString()
            )

            sharedPre!!.saveString(
                AppConstants.STUDY_LEVEL,
                selected?.label.toString()
            )

            if (selected != null) {

                val request =
                    SavePreferencesRequest(
                        disciplines =
                            selectedDiscipline,

                        destination_country =
                            selectedCountry!!,

                        preferred_study_level =
                            selected.label
                    )

                Log.d(
                    "SelectedStudyLevelItem",
                    request.toString()
                )

                ViewModalClass()
                    .savePreferencesDataList(
                        requireActivity(),
                        AppConstants.fiClientNumber,
                        sharedPre?.getString(
                            AppConstants.Device_IDENTIFIER,
                            ""
                        ) ?: "",
                        "Bearer ${CommonUtils.accessToken}",
                        request
                    )
                    .observe(viewLifecycleOwner) { savePreferences ->

                        savePreferences?.let {

                            if (it.statusCode == 200) {

                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        MainActivity::class.java
                                    )
                                )

                            } else if (it.statusCode == 422) {

                                CommonUtils.toast(
                                    requireActivity(),
                                    it.message
                                        ?: "Something went wrong"
                                )
                            }
                        }
                    }

            } else {

                Toast.makeText(
                    requireContext(),
                    "Please select a study level",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setApplicationActiveRecyclerview() {

        val layoutManager =
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

        binding.rvFaActive.layoutManager =
            layoutManager

        adapterScheduledAdapter =
            StudyLevelAdapter(
                requireActivity(),
                studyLevelList,
                this
            )

        binding.rvFaActive.adapter =
            adapterScheduledAdapter
    }

    override fun onSelect(
        data: Data?,
        position1: Int
    ) {

    }
}