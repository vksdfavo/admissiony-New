package com.student.Compass_Abroad.fragments.setPrefrences

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.setPrefrences.DisciplineAdaptor
import com.student.Compass_Abroad.databinding.FragmentDisciplineBinding
import com.student.Compass_Abroad.modal.preferCountryList.Data
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.Compass_Abroad.viewmodel.SetPreferencesViewModel

class DisciplineFragment : Fragment() {

    private lateinit var binding: FragmentDisciplineBinding
    private var disciplineAdapter: DisciplineAdaptor? = null
    private val allDisciplineList = mutableListOf<Data>()
    private lateinit var viewModel: SetPreferencesViewModel

    // ✅ Loading flag
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDisciplineBinding.inflate(inflater, container, false)

        binding.view1.setBackgroundResource(R.color.secondary_color)
        binding.view2.setBackgroundResource(R.color.secondary_color)
        binding.view3.setBackgroundResource(R.color.bottom_nav_grey)

        viewModel =
            ViewModelProvider(requireActivity())[SetPreferencesViewModel::class.java]

        // ✅ Disable button until API loaded
        binding.tvNext2.isEnabled = false

        // ✅ Fetch API disciplines first
        fetchPreferencesAndLoadDisciplines()

        binding.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tvNext2.setOnClickListener {

            // ✅ Prevent fast click before API load
            if (!isDataLoaded) {
                CommonUtils.toast(
                    requireContext(),
                    "Please wait, loading disciplines..."
                )
                return@setOnClickListener
            }

            val selectedDisciplines = viewModel.selectedDisciplines

            if (selectedDisciplines.isNotEmpty()) {

                selectedDisciplines.forEach {
                    Log.d(
                        "SelectedDisciplineItem",
                        "Label: ${it.label}, Value: ${it.value}"
                    )
                }

                val selectedValue = selectedDisciplines
                    .mapNotNull { it.label?.trim() }
                    .filter { it.isNotBlank() }

                sharedPre?.saveString(
                    AppConstants.USER_DISCIPLINES,
                    selectedValue.toString()
                )

                val bundle = Bundle().apply {
                    putString(
                        "request_json",
                        Gson().toJson(selectedValue)
                    )
                }

                binding.root.findNavController()
                    .navigate(R.id.studyLevelFragment2, bundle)

            } else {

                CommonUtils.toast(
                    requireContext(),
                    "Please select at least one discipline"
                )
            }
        }

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }

        return binding.root
    }

    // ✅ Step 1: Fetch preferences
    private fun fetchPreferencesAndLoadDisciplines() {

        LoginViewModal().getPreferencesDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { prefResponse ->

            val apiSelectedLabels = mutableListOf<String>()

            if (prefResponse?.statusCode == 200) {

                val info = prefResponse.data?.preferencesInfo
                val disciplines = info?.disciplines ?: info?.discipline

                val parsed = when (disciplines) {

                    is List<*> -> {
                        disciplines.filterIsInstance<String>()
                            .map { it.trim() }
                    }

                    is String -> {
                        disciplines.split(",")
                            .map { it.trim() }
                    }

                    else -> emptyList()
                }

                apiSelectedLabels.addAll(parsed)

                Log.d(
                    "API_Disciplines",
                    "From API: $apiSelectedLabels"
                )
            }

            // ✅ Load discipline list
            loadDisciplineList(apiSelectedLabels)
        }
    }

    // ✅ Step 2: Load all disciplines
    private fun loadDisciplineList(
        selectedLabels: List<String>
    ) {

        ViewModalClass().getDisciplineDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(
                AppConstants.Device_IDENTIFIER,
                ""
            ) ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->

            if (response?.success == true) {

                response.data?.let {

                    allDisciplineList.clear()
                    allDisciplineList.addAll(it)

                    // ✅ Match API disciplines
                    val previouslySelected =
                        allDisciplineList.filter { dataItem ->

                            selectedLabels.any { label ->

                                label.equals(
                                    dataItem.label?.trim(),
                                    ignoreCase = true
                                )
                            }
                        }

                    // ✅ Store selected
                    viewModel.selectedDisciplines.clear()
                    viewModel.selectedDisciplines.addAll(previouslySelected)

                    Log.d(
                        "PreSelected",
                        "Count: ${previouslySelected.size}"
                    )

                    disciplineAdapter = DisciplineAdaptor(
                        requireContext(),
                        allDisciplineList,
                        viewModel.selectedDisciplines.toSet(),

                        object : DisciplineAdaptor.Select {

                            override fun onItemToggled(
                                item: Data,
                                isSelected: Boolean
                            ) {

                                if (isSelected) {

                                    if (!viewModel.selectedDisciplines.contains(item)) {
                                        viewModel.selectedDisciplines.add(item)
                                    }

                                } else {

                                    viewModel.selectedDisciplines.remove(item)
                                }
                            }
                        }
                    )

                    binding.disciplineRecyclerview.apply {

                        layoutManager = StaggeredGridLayoutManager(
                            3,
                            StaggeredGridLayoutManager.VERTICAL
                        )

                        adapter = disciplineAdapter
                    }

                    // ✅ API & Recycler loaded
                    isDataLoaded = true

                    // ✅ Enable next button
                    binding.tvNext2.isEnabled = true
                }
            }
        }
    }
}