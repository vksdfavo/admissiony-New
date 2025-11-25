package com.student.Compass_Abroad.fragments.setPrefrences

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.PixelCopy.request
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.SavePreferencesRequest
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.StudyLevelAdapter
import com.student.Compass_Abroad.databinding.FragmentStudyLevelBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.studyLevelModel.Data
import com.student.Compass_Abroad.retrofit.ViewModalClass

class StudyLevelFragment :  Fragment(), StudyLevelAdapter.SelectStudyLevel {
    private lateinit var binding: FragmentStudyLevelBinding
    private var adapterScheduledAdapter: StudyLevelAdapter? = null
    private val studyLevelList: MutableList<Data> = mutableListOf()
    private var selectedCountry: String? = ""


    private val viewModel: ViewModalClass by lazy {
        ViewModalClass()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLevelBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        binding.view1.setBackgroundResource(R.color.secondary_color)       // green
        binding.view2.setBackgroundResource(R.color.secondary_color)       // grey
        binding.view3.setBackgroundResource(R.color.secondary_color)       // grey


        selectedCountry = App.singleton?.selectedCountry

        val selectedDisciplinesJson = arguments?.getString("request_json")

        val type = object : TypeToken<List<String>>() {}.type
        val selectedDisciplines: List<String> = Gson().fromJson(selectedDisciplinesJson, type)

// ✅ Log it as a proper JSON array string
        val jsonFormattedLog = Gson().toJson(selectedDisciplines)
        Log.d("SelectedStudyLevelItem", jsonFormattedLog)


        fetchDataFromApi()
        setApplicationActiveRecyclerview()
        onClicks(selectedDisciplines)


        return binding.root
    }

    private fun onClicks(selectedDiscipline: List<String>) {
        binding.fabAcBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        binding.btnSubmit.setOnClickListener {
            val selected = adapterScheduledAdapter?.getSelectedItem()

            Log.d("SelectedStudyLevelItem", selected?.label.toString())

            sharedPre!!.saveString(AppConstants.STUDY_LEVEL, selected?.label.toString())

            if (selected != null) {
                val request = SavePreferencesRequest(
                    disciplines = selectedDiscipline,
                    destination_country = selectedCountry.toString(),
                    preferred_study_level = selected.label
                )

                ViewModalClass().savePreferencesDataList(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer " + CommonUtils.accessToken,
                    request
                ).observe(viewLifecycleOwner) { savePreferences ->
                    savePreferences?.let {
                        if (it.statusCode == 200) {
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                        } else if (it.statusCode==422){

                            CommonUtils.toast(
                                requireActivity(),
                                it.message ?: "Something went wrong"
                            )
                        }
                    }
                }
            } else {

                Toast.makeText(requireContext(), "Please select a study level", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setApplicationActiveRecyclerview() {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFaActive.layoutManager = layoutManager

        adapterScheduledAdapter = StudyLevelAdapter(requireActivity(), studyLevelList, this)
        binding.rvFaActive.adapter = adapterScheduledAdapter
    }

    private fun fetchDataFromApi() {
        viewModel.getStudyLevelModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data ?: emptyList()
                    studyLevelList.addAll(programResponse)
                    adapterScheduledAdapter?.notifyDataSetChanged()

                    // Restore saved selection
                    val savedStudyLevel = sharedPre?.getString(AppConstants.STUDY_LEVEL, "")
                    adapterScheduledAdapter?.setPreSelectedLabel(savedStudyLevel)

                    // Show/hide views
                    if (studyLevelList.isEmpty()) {
                        binding.llFaActiveNoApplications.visibility = View.VISIBLE
                        binding.rvFaActive.visibility = View.GONE
                    } else {
                        binding.llFaActiveNoApplications.visibility = View.GONE
                        binding.rvFaActive.visibility = View.VISIBLE
                    }
                }

            } ?: run {
                binding!!.llFaActiveNoApplications.visibility = View.VISIBLE
                binding!!.rvFaActive.visibility = View.GONE
            }

            binding!!.pbFaActive.visibility = View.GONE
        }
    }

    override fun onSelect(
        data: Data?,
        position1: Int
    ) {
    }

    override fun onResume() {
        super.onResume()

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)
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
    }
}