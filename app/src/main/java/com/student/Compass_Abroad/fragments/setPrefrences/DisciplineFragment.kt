package com.student.Compass_Abroad.fragments.setPrefrences

import android.content.Intent
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
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.preferCountryList.Data
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.Compass_Abroad.viewmodel.SetPreferencesViewModel
import org.json.JSONArray

class DisciplineFragment : Fragment() {

    private lateinit var binding: FragmentDisciplineBinding
    private var disciplineAdapter: DisciplineAdaptor? = null
    private val allDisciplineList = mutableListOf<Data>()
    private lateinit var viewModel: SetPreferencesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisciplineBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        binding.view1.setBackgroundResource(R.color.secondary_color)
        binding.view2.setBackgroundResource(R.color.secondary_color)
        binding.view3.setBackgroundResource(R.color.bottom_nav_grey)

        viewModel = ViewModelProvider(requireActivity())[SetPreferencesViewModel::class.java]

        Log.d(
            "SelectedCountry_sharepref",

            sharedPre!!.getString(AppConstants.USER_DISCIPLINES, "").toString()
        )

        getDisciplineList()

        binding.fabAcBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tvNext2.setOnClickListener {
            val selectedDisciplines = viewModel.selectedDisciplines

            if (selectedDisciplines.isNotEmpty()) {

                selectedDisciplines.forEach {

                    Log.d("SelectedDisciplineItem", "Label: ${it.label}, Value: ${it.value}")

                }

                val selectedValue = viewModel.selectedDisciplines.mapNotNull { it.label?.trim() }

                    .filter { it.isNotBlank() }

                sharedPre?.saveString(AppConstants.USER_DISCIPLINES, selectedValue.toString())



                val bundle = Bundle().apply {
                    putString("request_json", Gson().toJson(selectedValue))
                }


                binding.root.findNavController().navigate(R.id.studyLevelFragment2, bundle)

            } else {
                CommonUtils.toast(requireContext(), "Please select at least one discipline")
            }
        }

        binding.tvSkip.setOnClickListener {

            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }

        return binding.root
    }

    private fun getDisciplineList() {
        val savedDisciplines = sharedPre?.getString(AppConstants.USER_DISCIPLINES, null)
        val selectedLabels = mutableListOf<String>()

        savedDisciplines?.let {
            try {
                selectedLabels.addAll(
                    it.removePrefix("[").removeSuffix("]").split(",").map { item -> item.trim() }
                )
            } catch (e: Exception) {
                Log.e("DisciplineFragment", "Error parsing saved disciplines", e)
            }
        }

        ViewModalClass().getDisciplineDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->
            if (response?.success == true) {
                response.data?.let {
                    allDisciplineList.clear()
                    allDisciplineList.addAll(it)

                    // Pre-select items that match saved labels
                    val previouslySelected = allDisciplineList.filter { dataItem ->
                        selectedLabels.contains(dataItem.label)
                    }

                    viewModel.selectedDisciplines.clear()
                    viewModel.selectedDisciplines.addAll(previouslySelected)

                    disciplineAdapter = DisciplineAdaptor(
                        requireContext(),
                        allDisciplineList,
                        viewModel.selectedDisciplines.toSet(),
                        object : DisciplineAdaptor.Select {
                            override fun onItemToggled(item: Data, isSelected: Boolean) {
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
                        layoutManager = StaggeredGridLayoutManager(3 ,StaggeredGridLayoutManager.VERTICAL)
                        adapter = disciplineAdapter
                    }
                }
            }
        }
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
