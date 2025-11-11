package com.student.Compass_Abroad.fragments.home

import AdapterFilterCollegeCountrySelector
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider

import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterFilterCollegeCitySelector
import com.student.Compass_Abroad.adaptor.AdapterFilterCollegeStateSelector

import com.student.Compass_Abroad.adaptor.AdapterFilterDisplineSelector

import com.student.Compass_Abroad.adaptor.AdapterFilterInstitutionSelector
import com.student.Compass_Abroad.adaptor.AdapterFilterIntakeSelector
import com.student.Compass_Abroad.adaptor.AdapterFilterStudyLevelSelector
import com.student.Compass_Abroad.databinding.FragmentProgramFilterBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.cityModel.CityModel
import com.student.Compass_Abroad.modal.countryModel.CountryResponse
import com.student.Compass_Abroad.modal.discipline.DisciplineModel

import com.student.Compass_Abroad.modal.institutionModel.InstitutionModel
import com.student.Compass_Abroad.modal.intakeModel.IntakeModel
import com.student.Compass_Abroad.modal.stateModel.stateModel
import com.student.Compass_Abroad.modal.studyLevelModel.Data
import com.student.Compass_Abroad.modal.studyLevelModel.StudyLevelModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.util.Locale


class ProgramFilterFragment : BaseFragment() {

    var   binding: FragmentProgramFilterBinding?=null
    var   popupWindow:PopupWindow?=null
    var   PGWP:String =""
    var   Attendance:String =""
    var   ProgramType:String =""
    var   accomodation:String =""
    var   minTutionFee: String = ""
    var   maxTutionFee: String = ""
    var   minApplicationFee: String = ""
    var   maxApllicationFee: String = ""
    private lateinit var   selectedInstitutionId:ArrayList<String>
    private  var   selectedCountryId:String=""

    private  var   selectedStateId:String=""
    private  var   selectedCityId:String=""
    private lateinit var   selectedStudyLevelId:ArrayList<String>
    private lateinit var  selectedDisciplineId:ArrayList<String>
    private lateinit var   selectedIntakeId:ArrayList<String>



    private lateinit var   selectedInstitutionLabel:ArrayList<String>
    private  var   selectedCountryLabel:String=""
    private  var   selectedStateLabel:String=""
    private  var   selectedCityLabel:String=""
    private lateinit var   selectedStudyLevelLabel:ArrayList<String>
    private lateinit var  selectedDisciplineLabel:ArrayList<String>
    private lateinit var   selectedIntakeLabel:ArrayList<String>


    var arrayListCountry=ArrayList<com.student.Compass_Abroad.modal.countryModel.DataX>()
    var arrayListState=ArrayList<com.student.Compass_Abroad.modal.stateModel.Data>()
    var arrayListCity=ArrayList<com.student.Compass_Abroad.modal.cityModel.Data>()

    var arrayListInstitution=ArrayList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>()
    private lateinit var selectedInstitutionItems: MutableList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>
    private lateinit var selectedCountryItems: com.student.Compass_Abroad.modal.countryModel.DataX
    private lateinit var selectedStateItems1: com.student.Compass_Abroad.modal.stateModel.Data
    private lateinit var selectedCityItems1: com.student.Compass_Abroad.modal.cityModel.Data


    private lateinit var selectedStateItems: String
    private lateinit var selectedCityItems: String

    private lateinit var selectedStudyLevelItems:MutableList<com.student.Compass_Abroad.modal.studyLevelModel.Data>
    private lateinit var selectedDisciplineItems:MutableList<com.student.Compass_Abroad.modal.discipline.Data>
    private lateinit var selectedIntakeItems:MutableList<com.student.Compass_Abroad.modal.intakeModel.Data>
    var arrayListStudyLevel=ArrayList<com.student.Compass_Abroad.modal.studyLevelModel.Data>()
    var arrayListDiscipline=ArrayList<com.student.Compass_Abroad.modal.discipline.Data>()
    var arrayListIntake=ArrayList<com.student.Compass_Abroad.modal.intakeModel.Data>()
    var fragment:Fragment?=null
    var xLocationOfView=0
    var yLocationOfView=0
    var value:Boolean=false




    companion object {
        var data: String? = null
        var clearData: Int? = null
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProgramFilterBinding.inflate(inflater,container,false)

        if (data.equals("language_program")) {
            binding!!.tvIntake.visibility = View.GONE
            binding!!.rlIntake.visibility = View.GONE
            binding!!.tvAttendance.visibility = View.GONE
            binding!!.rlAttendance.visibility = View.GONE
            binding!!.tvProgramType.visibility = View.GONE
            binding!!.rlProgramType.visibility = View.GONE

            binding!!.tvTutionList.visibility = View.GONE
            binding!!.tvFilterRs1.visibility = View.GONE
            binding!!.tvFilterRs11.visibility = View.GONE
            binding!!.rsFilter1.visibility = View.GONE

            binding!!.tvApplicationList11.visibility = View.GONE
            binding!!.tvFilterRs2.visibility = View.GONE
            binding!!.tvFilterRs22.visibility = View.GONE
            binding!!.rsFilter2.visibility = View.GONE


            binding!!.tvAccomodation.visibility = View.VISIBLE
            binding!!.rlAccomodation.visibility = View.VISIBLE

            binding!!.tvEnglishLevel.visibility = View.GONE
            binding!!.rlEnglishLevel.visibility = View.GONE

            binding!!.tvAge.visibility = View.GONE
            binding!!.rlAge.visibility = View.GONE


        } else if (data == "summer_school") {
            binding!!.tvIntake.visibility = View.GONE
            binding!!.rlIntake.visibility = View.GONE

            binding!!.tvAttendance.visibility = View.GONE
            binding!!.rlAttendance.visibility = View.GONE


            binding!!.tvProgramType.visibility = View.GONE
            binding!!.rlProgramType.visibility = View.GONE

            binding!!.tvTutionList.visibility = View.GONE
            binding!!.tvFilterRs1.visibility = View.GONE
            binding!!.tvFilterRs11.visibility = View.GONE
            binding!!.rsFilter1.visibility = View.GONE

            binding!!.tvApplicationList11.visibility = View.GONE
            binding!!.tvFilterRs2.visibility = View.GONE
            binding!!.tvFilterRs22.visibility = View.GONE
            binding!!.rsFilter2.visibility = View.GONE


            binding!!.tvStudyLevel.visibility = View.GONE
            binding!!.tvFilterStudyLevel.visibility = View.GONE


            binding!!.tvlookingFor.visibility = View.GONE
            binding!!.tvFilterLookingFor.visibility = View.GONE

            binding!!.tvIntake.visibility = View.GONE
            binding!!.rlIntake.visibility = View.GONE


            binding!!.tvAccomodation.visibility = View.GONE
            binding!!.rlAccomodation.visibility = View.GONE

            binding!!.tvEnglishLevel.visibility = View.GONE
            binding!!.rlEnglishLevel.visibility = View.GONE

            binding!!.tvAge.visibility = View.GONE
            binding!!.rlAge.visibility = View.GONE

            binding!!.tvPGMP.visibility = View.GONE
            binding!!.tvFilterPGMP.visibility = View.GONE
        }
        else if (data == "career_program") {
            binding!!.tvIntake.visibility = View.GONE
            binding!!.rlIntake.visibility = View.GONE

            binding!!.tvAttendance.visibility = View.GONE
            binding!!.rlAttendance.visibility = View.GONE


            binding!!.tvProgramType.visibility = View.GONE
            binding!!.rlProgramType.visibility = View.GONE

            binding!!.tvTutionList.visibility = View.GONE
            binding!!.tvFilterRs1.visibility = View.GONE
            binding!!.tvFilterRs11.visibility = View.GONE
            binding!!.rsFilter1.visibility = View.GONE

            binding!!.tvApplicationList11.visibility = View.GONE
            binding!!.tvFilterRs2.visibility = View.GONE
            binding!!.tvFilterRs22.visibility = View.GONE
            binding!!.rsFilter2.visibility = View.GONE


            binding!!.tvStudyLevel.visibility = View.VISIBLE
            binding!!.tvFilterStudyLevel.visibility = View.VISIBLE


            binding!!.tvlookingFor.visibility = View.VISIBLE
            binding!!.tvFilterLookingFor.visibility = View.VISIBLE

            binding!!.tvIntake.visibility = View.GONE
            binding!!.rlIntake.visibility = View.GONE


            binding!!.tvAccomodation.visibility = View.GONE
            binding!!.rlAccomodation.visibility = View.GONE

            binding!!.tvEnglishLevel.visibility = View.GONE
            binding!!.rlEnglishLevel.visibility = View.GONE

            binding!!.tvAge.visibility = View.GONE
            binding!!.rlAge.visibility = View.GONE

            binding!!.tvPGMP.visibility = View.VISIBLE
            binding!!.tvFilterPGMP.visibility = View.VISIBLE
        }

        else {

            binding!!.tvIntake.visibility = View.VISIBLE
            binding!!.rlIntake.visibility = View.VISIBLE
            binding!!.tvAttendance.visibility = View.VISIBLE
            binding!!.rlAttendance.visibility = View.VISIBLE
            binding!!.tvProgramType.visibility = View.VISIBLE
            binding!!.rlProgramType.visibility = View.VISIBLE

            binding!!.tvTutionList.visibility = View.VISIBLE
            binding!!.tvFilterRs1.visibility = View.VISIBLE
            binding!!.tvFilterRs11.visibility = View.VISIBLE
            binding!!.rsFilter1.visibility = View.VISIBLE

            binding!!.tvApplicationList11.visibility = View.VISIBLE
            binding!!.tvFilterRs2.visibility = View.VISIBLE
            binding!!.tvFilterRs22.visibility = View.VISIBLE
            binding!!.rsFilter2.visibility = View.VISIBLE

            binding!!.tvAccomodation.visibility = View.GONE
            binding!!.rlAccomodation.visibility = View.GONE

            binding!!.tvEnglishLevel.visibility = View.GONE
            binding!!.rlEnglishLevel.visibility = View.GONE

            binding!!.tvAge.visibility = View.GONE
            binding!!.rlAge.visibility = View.GONE

        }

        if (clearData == 1) {
            binding!!.tvFilterClearAll.visibility = View.GONE
        } else {
            binding!!.tvFilterClearAll.visibility = View.VISIBLE
        }


        initilalizeList()

        val previousCountryIds=getSavedSelectedItemId(AppConstants.CountryList)
        val previousCountryLabels=getSavedSelectedItemLabel(AppConstants.CountryList)


        val previousStateIds=getSavedSelectedItemId(AppConstants.StateList)
        val previousStateLabels=getSavedSelectedItemLabel(AppConstants.StateList)


        val previousCityIds=getSavedSelectedItemId(AppConstants.CityList)
        val previousCityLabels=getSavedSelectedItemLabel(AppConstants.CityList)




        val studyLevel = getSavedStudyLevelItems(AppConstants.studyLevelList)
        val previousStudyLevelIds = studyLevel.map { it.value.toString() }
        val previousStudyLevelLabels = studyLevel.map { it.label }

        val displine = getSavedDisciplineItems(AppConstants.disciplineList)

        val previousDisciplineIds = displine.map { it.value.toString() }
        val previousDisciplineLabels = displine.map { it.label }

        val intakes = getSavedIntakesItems(AppConstants.IntakeList)

        val previousIntakeIds = intakes.map { it.value.toString() }
        val previousIntakeLabels = intakes.map { it.label }

        val institution = getSavedInstutionItems(AppConstants.institutionList)

        val previousInstitutionIds = institution.map { it.value.toString() }
        val previousInstitutionLabels = institution.map { it.label }

        val sharedPrefs = SharedPrefs(requireContext())

        PGWP = sharedPrefs.getString11(AppConstants.PGWP_KEY)
        Attendance = sharedPrefs.getString11(AppConstants.ATTENDANCE_KEY)
        ProgramType = sharedPrefs.getString11(AppConstants.PROGRAM_TYPE_KEY)
        accomodation = sharedPrefs.getString11(AppConstants.Accomodation)
        minTutionFee = sharedPrefs.getString11(AppConstants.MIN_TUTION_KEY)
        maxTutionFee = sharedPrefs.getString11(AppConstants.MAX_TUTION_KEY)
        minApplicationFee = sharedPrefs.getString11(AppConstants.MIN_APPLICATION_KEY)
        maxApllicationFee = sharedPrefs.getString11(AppConstants.MAX_APPLICATION_KEY)




// Process the data
        val formattedCountryIds = previousCountryIds
        val formattedCountryLabels = previousCountryLabels


        val formattedInstitutionIds = previousInstitutionIds
        val formattedInstitutionLabels = previousInstitutionLabels

        val formattedIntakeIds = previousIntakeIds
        val formattedIntakeLabels = previousIntakeLabels

        val formattedDisciplineIds = previousDisciplineIds
        val formattedDisciplineLabels = previousDisciplineLabels

        val formattedStudyLevelIds = previousStudyLevelIds
        val formattedStudyLevelLabels = previousStudyLevelLabels

        val formattedStateIds = previousStateIds
        val formattedStateLabels = previousStateLabels

        val formattedCityIds = previousCityIds
        val formattedCityLabels = previousCityLabels

        selectedStudyLevelItems = studyLevel.toMutableList()

        selectedDisciplineItems = displine.toMutableList()

        selectedIntakeItems = intakes.toMutableList()

        selectedInstitutionItems = institution.toMutableList()


// Example of setting data
        setData(formattedCountryIds, formattedInstitutionIds, formattedIntakeIds, formattedDisciplineIds, formattedStudyLevelIds, formattedStateIds,
            formattedCityIds)
        updateUIWithFilterData(formattedCountryLabels, formattedInstitutionLabels, formattedIntakeLabels, formattedDisciplineLabels, formattedStudyLevelLabels, formattedStateLabels,
            formattedCityLabels)
// Save values
        saveSelectedToSharedPreferences(AppConstants.CountryList, formattedCountryIds, formattedCountryLabels)
        saveSelectedToSharedPreferences(
            AppConstants.StateList,
            formattedStateIds,
            formattedStateLabels
        )
        saveSelectedToSharedPreferences(
            AppConstants.CityList,
            formattedCityIds,
            formattedCityLabels
        )
        saveSelectedValuesToSharedPreferences(AppConstants.institutionList, formattedInstitutionIds, formattedInstitutionLabels)
        saveSelectedValuesToSharedPreferences(AppConstants.IntakeList, formattedIntakeIds, formattedIntakeLabels)
        saveSelectedValuesToSharedPreferences(AppConstants.disciplineList, formattedDisciplineIds, formattedDisciplineLabels)
        saveSelectedValuesToSharedPreferences(AppConstants.studyLevelList, formattedStudyLevelIds, formattedStudyLevelLabels)

        saveValues(minTutionFee, maxTutionFee, minApplicationFee, maxApllicationFee)

// Set dat
        fragment = this
        apicall()
        setDropDown()
        binding!!.tvFilterClearAll.setOnClickListener {

            PGWP=""
            Attendance=""
            ProgramType=""
            accomodation=""


            selectedCountryItems.value=0
            selectedCountryItems.label=""
            selectedStateItems1.value = 0
            selectedStateItems1.label = ""
            selectedCityItems1.value = 0
            selectedCityItems1.label = ""
            selectedCityItems=""
            selectedStateItems=""
            selectedInstitutionItems.clear()
            selectedIntakeItems.clear()
            selectedStudyLevelItems.clear()
            selectedDisciplineItems.clear()

            selectedCountryId=""
            selectedStateId=""
            selectedCityId=""
            selectedInstitutionId.clear()
            selectedStudyLevelId.clear()
            selectedDisciplineId.clear()
            selectedIntakeId.clear()

            selectedCountryLabel=""
            selectedStateLabel=""
            selectedCityLabel=""
            selectedInstitutionLabel.clear()
            selectedStudyLevelLabel.clear()
            selectedDisciplineLabel.clear()
            selectedIntakeLabel.clear()


            binding!!.tvFilterCountry.text = ""
            binding!!.tvFilterState.text = ""
            binding!!.tvFilterCity.text = ""
            binding!!.tvFilterInstitute.text = ""
            binding!!.tvFilterPGMP.text = ""
            binding!!.tvFilterStudyLevel.text = ""
            binding!!.tvFilterLookingFor.text = ""
            binding!!.tvFilterIntakeSelector.text = ""
            binding!!.tvFilterProgramType.text = ""
            binding!!.tvFilterAccomodation.text = ""
            binding!!.tvFilterAttendance.text = ""


            binding!!.rsFilter1.values = listOf(0f, 244000f)
            binding!!.tvFilterRs1.text = String.format(Locale.getDefault(), "%.0f - %.0f", 0f, 244000f)
            minTutionFee=""
            maxTutionFee=""
            minApplicationFee=""
            maxApllicationFee=""

            // Reset application fee slider
            binding!!.rsFilter2.values = listOf(0f, 6200f)
            binding!!.tvFilterRs2.text = String.format(Locale.getDefault(), "%.0f - %.0f", 0f, 6200f)

            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.Accomodation)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)
            arrayListInstitution.clear()
            clearAllSelectedValues()


        }


        binding!!.fabFilterBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        binding!!.tvFilterApply.setOnClickListener {

            // Check if nothing is selected
            val noFilterSelected =
                selectedCountryId.isNullOrEmpty() &&
                        selectedStateId.isNullOrEmpty() &&
                        selectedCityId.isNullOrEmpty() &&
                        selectedInstitutionId.isNullOrEmpty() &&
                        selectedStudyLevelId.isNullOrEmpty() &&
                        selectedDisciplineId.isNullOrEmpty() &&
                        selectedIntakeId.isNullOrEmpty() &&
                        PGWP.isNullOrEmpty() &&
                        Attendance.isNullOrEmpty() &&
                        ProgramType.isNullOrEmpty() &&
                        accomodation.isNullOrEmpty()&&
                        (minTutionFee.isNullOrEmpty() || maxTutionFee.isNullOrEmpty()) &&
                        (minApplicationFee.isNullOrEmpty() || maxApllicationFee.isNullOrEmpty())

            if (noFilterSelected) {
                Toast.makeText(requireActivity(), "Please select at least one filter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save filters
            saveSelectedToSharedPreferences(AppConstants.CountryList, selectedCountryId, selectedCountryLabel)
            saveSelectedToSharedPreferences(AppConstants.StateList, selectedStateId, selectedStateLabel)
            saveSelectedToSharedPreferences(AppConstants.CityList, selectedCityId, selectedCityLabel)
            saveSelectedValuesToSharedPreferences(AppConstants.institutionList, selectedInstitutionId, selectedInstitutionLabel)
            saveSelectedValuesToSharedPreferences(AppConstants.studyLevelList, selectedStudyLevelId, selectedStudyLevelLabel)
            saveSelectedValuesToSharedPreferences(AppConstants.disciplineList, selectedDisciplineId, selectedDisciplineLabel)
            saveSelectedValuesToSharedPreferences(AppConstants.IntakeList, selectedIntakeId, selectedIntakeLabel)

            saveValues(minTutionFee, maxTutionFee, minApplicationFee, maxApllicationFee)

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        // Inflate the layout for this fragment
        return binding!!.getRoot()
    }

    private fun setData(
        previousCountryIds: String,
        previousInstitutionIds: List<String>,
        previousIntakeIds: List<String>,
        previousDisciplineIds: List<String>,
        previousStudyLevelIds: List<String>,
        previousStateIds: String,
        formattedCityIds: String,
    ) {
        if (!previousCountryIds.isNullOrEmpty() && previousCountryIds.all { it.isDigit() }) {
            selectedCountryId = previousCountryIds
            selectedCountryItems.value = previousCountryIds.toInt()
        } else {
            selectedCountryId = ""
            selectedCountryItems.value = 0 // Or set to a default value
        }
        selectedInstitutionId = ArrayList(previousInstitutionIds)
        selectedStudyLevelId = ArrayList(previousStudyLevelIds)
        selectedDisciplineId = ArrayList(previousDisciplineIds)
        selectedIntakeId = ArrayList(previousIntakeIds)

        // PGWP and Attendance should be set if they are not null
        PGWP = PGWP ?: ""
        Attendance = Attendance ?: ""
        ProgramType = ProgramType ?: ""
        accomodation = accomodation ?: ""
        minTutionFee = minTutionFee ?: ""
        maxTutionFee = maxTutionFee ?: ""
        minApplicationFee = minApplicationFee ?: ""
        maxApllicationFee = maxApllicationFee ?: ""
        if (!previousStateIds.isNullOrEmpty() && previousStateIds.all { it.isDigit() }) {
            selectedStateId = previousStateIds
            selectedStateItems1.value = previousStateIds.toInt()
        } else {
            selectedStateId = ""
            selectedStateItems1.value = 0 // Or set to a default value
        }

        if (!formattedCityIds.isNullOrEmpty() && formattedCityIds.all { it.isDigit() }) {
            selectedCityId = formattedCityIds
            selectedCityItems1.value = formattedCityIds.toInt()
        } else {
            selectedCityId = ""
            selectedCityItems1.value = 0 // Or set to a default value
        }
    }

    private fun updateUIWithFilterData(
        previousCountryLabels:String,
        previousInstitutionLabels: List<String>,
        previousIntakeLabels: List<String>,
        previousDisciplineLabels: List<String>,
        previousStudyLevelLabels: List<String>,
        formattedStateLabels: String,
        formattedCityLabels: String,
    ) {
        // Update selected labels
        selectedCountryLabel=previousCountryLabels
        selectedCountryItems.label = previousCountryLabels
        selectedInstitutionLabel = ArrayList(previousInstitutionLabels)
        selectedStudyLevelLabel = ArrayList(previousStudyLevelLabels)
        selectedDisciplineLabel = ArrayList(previousDisciplineLabels)
        selectedIntakeLabel = ArrayList(previousIntakeLabels)
        selectedStateLabel = formattedStateLabels
        selectedStateItems1.label = formattedStateLabels
        selectedCityLabel = formattedCityLabels
        selectedCityItems1.label = formattedCityLabels

        // Set text to EditText fields
        binding!!.tvFilterCountry.setText(selectedCountryLabel)
        binding!!.tvFilterState.setText(selectedStateLabel)
        binding!!.tvFilterCity.setText(selectedCityLabel)
        binding!!.tvFilterInstitute.setText(selectedInstitutionLabel.joinToString("\n"))
        binding!!.tvFilterStudyLevel.setText(selectedStudyLevelLabel.joinToString("\n"))
        binding!!.tvFilterLookingFor.setText(selectedDisciplineLabel.joinToString("\n"))
        binding!!.tvFilterIntakeSelector.setText(selectedIntakeLabel.joinToString("\n"))
        binding!!.tvFilterPGMP.setText(PGWP)
        binding!!.tvFilterAttendance.setText(Attendance)
        binding!!.tvFilterAccomodation.setText(accomodation)


        // Program Type
        when (ProgramType) {
            "full_time" -> binding!!.tvFilterProgramType.text = "Full Time"
            "part_time" -> binding!!.tvFilterProgramType.text = "Part Time"
        }


        val minFee = minTutionFee.toFloatOrNull() ?: 0f
        val maxFee = maxTutionFee.toFloatOrNull() ?: 0f

        binding!!.tvFilterRs1.setText(
            String.format(
                Locale.getDefault(),
                "%.0f - %.0f",
                minFee,
                maxFee
            )
        )

        binding!!.rsFilter1.setValues(minFee, maxFee)


        val minApplicationFee = minApplicationFee.toFloatOrNull() ?: 0f
        val maxApplicationFee = maxApllicationFee.toFloatOrNull() ?: 0f

        binding!!.tvFilterRs2.setText(
            String.format(
                Locale.getDefault(),
                "%.0f - %.0f",
                minApplicationFee,
                maxApplicationFee
            )
        )

        binding!!.rsFilter2.setValues(minApplicationFee, maxApplicationFee)


    }





    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDown() {
        setDropDownCountry()
        setDropDownState()
        setDropDownCity()
        setDropDownInstitution()
        setDropDownStudyLevel()
        setDropDownDiscipline()
        setDropDownIntake()
        setupPFMIDropdown()
        setupAccomodationDropdown()
        setupAttendanceDropdown()
        setupProgramTypeDropdown()
    }

    private fun apicall() {
        getCountry(requireActivity())

        if (!selectedCountryId.isNullOrEmpty() && !selectedStateId.isNullOrEmpty()) {
            getState(requireActivity(),selectedCountryId.toInt())
             getCity(requireActivity(),selectedStateId.toInt())
            arrayListInstitution.clear()
            getInstitution(requireActivity(),getSavedSelectedItemId(AppConstants.CountryList))

        } else if (!selectedCountryId.isNullOrEmpty()) {
             getState(requireActivity(),selectedCountryId.toInt())
            arrayListInstitution.clear()
            getInstitution(requireActivity(),getSavedSelectedItemId(AppConstants.CountryList))

        } else {
            getCountry(requireActivity())
        }

        getStudyLevel(requireActivity())
        getDisciplineLevel(requireActivity())
        getIntake(requireActivity())
    }

    private fun initilalizeList() {


        selectedInstitutionItems= mutableListOf()
        selectedStudyLevelItems= mutableListOf()
        selectedDisciplineItems= mutableListOf()
        selectedIntakeItems= mutableListOf()


        selectedInstitutionId= ArrayList<String>()

        selectedStudyLevelId= ArrayList<String>()
        selectedDisciplineId= ArrayList<String>()
        selectedIntakeId=ArrayList<String>()

        selectedInstitutionLabel= ArrayList<String>()

        selectedStudyLevelLabel= ArrayList<String>()
        selectedDisciplineLabel= ArrayList<String>()
        selectedIntakeLabel=ArrayList<String>()

        selectedCountryItems = com.student.Compass_Abroad.modal.countryModel.DataX("", 0)

        selectedStateItems1 = com.student.Compass_Abroad.modal.stateModel.Data("", 0)
        selectedCityItems1 = com.student.Compass_Abroad.modal.cityModel.Data("", 0)

        // Set up rsFilter1
        // Tuition Fee slider
        binding!!.rsFilter1.apply {
            valueFrom = 0f
            valueTo = 244000f
            stepSize = 0f

            post {
                val min = minTutionFee.toFloatOrNull() ?: 0f
                val max = maxTutionFee.toFloatOrNull() ?:244000f
                values = listOf(min, max)

                binding!!.tvFilterRs1.text = String.format(
                    Locale.getDefault(), "%.0f - %.0f", min, max
                )
            }
        }



        binding!!.rsFilter1.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Handle when the user starts touching the slider
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {

                minTutionFee = Math.round(slider.values[0]).toString()
                maxTutionFee = Math.round(slider.values[1]).toString()

                binding!!.tvFilterRs1.text = "Selected Range :${minTutionFee + "-" + maxTutionFee}"
            }
        })


// Application Fee slider
        binding!!.rsFilter2.apply {
            valueFrom = 0f
            valueTo = 6200f
            stepSize = 0f

            post {
                val min = minApplicationFee.toFloatOrNull() ?: 0f
                val max = maxApllicationFee.toFloatOrNull() ?:6200f
                values = listOf(min, max)

                binding!!.tvFilterRs2.text = String.format(
                    Locale.getDefault(), "%.0f - %.0f", min, max
                )
            }
        }

        binding!!.rsFilter2.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Handle when the user starts touching the slider
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {

                minApplicationFee = Math.round(slider.values[0]).toString()
                maxApllicationFee = Math.round(slider.values[1]).toString()

                binding!!.tvFilterRs2.text = "Selected Range :${minApplicationFee + "-" + maxApllicationFee}"
            }
        })

        binding!!.tvFilterRs11.setText("Selected Range-US$0- US$244k")
        binding!!.tvFilterRs22.setText("Selected Range-US$0- US$6.2k")

    }

    @SuppressLint("MissingInflatedId")
    private fun setupPFMIDropdown() {

        binding!!.tvFilterPGMP.setOnClickListener {
            val popupView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_popup, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterPGMP.width

            // Get option views
            val tvYes = popupView.findViewById<TextView>(R.id.tvYes)
            val tvNo = popupView.findViewById<TextView>(R.id.tvNo)

            // Reset any old ticks
            tvYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            // ✅ Add tick to selected one
            if (PGWP.equals("yes", ignoreCase = true)) {
                tvYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            } else if (PGWP.equals("no", ignoreCase = true)) {
                tvNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            }

            tvYes.setOnClickListener {
                binding!!.tvFilterPGMP.text = tvYes.text
                PGWP = "yes"
                popupWindow.dismiss()
            }

            tvNo.setOnClickListener {
                binding!!.tvFilterPGMP.text = tvNo.text
                PGWP = "no"
                popupWindow.dismiss()
            }

            // Show popup
            popupWindow.showAsDropDown(binding!!.tvFilterPGMP)
        }
    }





    @SuppressLint("MissingInflatedId")
    private fun setupAccomodationDropdown() {

        binding!!.tvFilterAccomodation.setOnClickListener {
            val popupView = LayoutInflater.from(requireActivity())
                .inflate(R.layout.custom_popup, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterPGMP.width

            // Option views
            val tvYes = popupView.findViewById<TextView>(R.id.tvYes)
            val tvNo = popupView.findViewById<TextView>(R.id.tvNo)

            // Remove any old ticks
            tvYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            // ✅ Show tick for currently selected accommodation
            if (accomodation.equals("yes", ignoreCase = true)) {
                tvYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            } else if (accomodation.equals("no", ignoreCase = true)) {
                tvNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            }

            // Handle clicks
            tvYes.setOnClickListener {
                accomodation = "yes"
                binding!!.tvFilterAccomodation.text = tvYes.text
                popupWindow.dismiss()
            }

            tvNo.setOnClickListener {
                accomodation = "no"
                binding!!.tvFilterAccomodation.text = tvNo.text
                popupWindow.dismiss()
            }

            // Show popup
            popupWindow.showAsDropDown(binding!!.tvFilterAccomodation)
        }
    }




    @SuppressLint("MissingInflatedId")
    private fun setupAttendanceDropdown() {

        binding!!.tvFilterAttendance.setOnClickListener {
            val popupView = LayoutInflater.from(requireActivity())
                .inflate(R.layout.custom_popup_attendance, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterAttendance.width

            // Option views
            val tvOnCampus = popupView.findViewById<TextView>(R.id.tvoncampus)
            val tvOnline = popupView.findViewById<TextView>(R.id.tvonline)
            val tvBlended = popupView.findViewById<TextView>(R.id.tvblended)

            // Remove old ticks
            tvOnCampus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvBlended.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            // ✅ Show tick for selected attendance type
            when (Attendance) {
                "On Campus" -> tvOnCampus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
                "Online" -> tvOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
                "Blended" -> tvBlended.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            }

            tvOnCampus.setOnClickListener {
                Attendance = "On Campus"
                binding!!.tvFilterAttendance.text = tvOnCampus.text
                popupWindow.dismiss()
            }

            tvOnline.setOnClickListener {
                Attendance = "Online"
                binding!!.tvFilterAttendance.text = tvOnline.text
                popupWindow.dismiss()
            }

            tvBlended.setOnClickListener {
                Attendance = "Blended"
                binding!!.tvFilterAttendance.text = tvBlended.text
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(binding!!.tvFilterAttendance)
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun setupProgramTypeDropdown() {

        binding!!.tvFilterProgramType.setOnClickListener {
            val popupView = LayoutInflater.from(requireActivity())
                .inflate(R.layout.custom_popup_programtype, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterProgramType.width

            // Option views
            val tvFullTime = popupView.findViewById<TextView>(R.id.tvfulltime)
            val tvPartTime = popupView.findViewById<TextView>(R.id.tvparttime)

            // Remove any old ticks
            tvFullTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvPartTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            // ✅ Show tick for selected ProgramType
            when (ProgramType) {
                "full_time" -> tvFullTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
                "part_time" -> tvPartTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_svgrepo_com, 0)
            }

            tvFullTime.setOnClickListener {
                ProgramType = "full_time"
                binding!!.tvFilterProgramType.text = tvFullTime.text
                popupWindow.dismiss()
            }

            tvPartTime.setOnClickListener {
                ProgramType = "part_time"
                binding!!.tvFilterProgramType.text = tvPartTime.text
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(binding!!.tvFilterProgramType)
        }
    }



    private fun setDropDownCountry() {
        binding!!.tvFilterCountry.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout
            layout.findViewById<TextView>(R.id.etSelect).hint = "Search Country"

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterCountry.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterFilterCollegeCountrySelector(requireActivity(), arrayListCountry, layout, object : AdapterFilterCollegeCountrySelector.OnCountrySelectionListener {
                override fun onCountryChecked(country: com.student.Compass_Abroad.modal.countryModel.DataX) {
                    clearSelectedValuesFromSharedPreferences(AppConstants.StateList)
                    clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
                    clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
                    selectedStateItems1.label = ""
                    selectedStateItems1.value = 0
                    selectedStateItems = ""
                    selectedStateId = ""
                    selectedStateLabel = ""
                    binding!!.tvFilterState.setText("")
                    arrayListState.clear()

                    selectedCityItems = ""
                    selectedCityId = ""
                    selectedCityLabel = ""
                    binding!!.tvFilterCity.setText("")
                    arrayListCity.clear()

                    getState(requireActivity(), country.value)


                    selectedInstitutionItems.clear()
                    selectedInstitutionId.clear()
                    selectedInstitutionLabel.clear()
                    arrayListInstitution.clear()
                    getInstitution(requireActivity(),country.value.toString())
                    setDropDownInstitution()
                    binding!!.tvFilterInstitute.text=""

                }

                override fun onCountryUnchecked(country: com.student.Compass_Abroad.modal.countryModel.DataX) {
                    clearSelectedValuesFromSharedPreferences(AppConstants.StateList)
                    clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
                    clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
                    selectedStateItems1.label = ""
                    selectedStateItems1.value = 0
                    selectedStateItems = ""
                    selectedStateId = ""
                    selectedStateLabel = ""
                    binding!!.tvFilterState.setText("")
                    arrayListState.clear()

                    selectedCityItems = ""
                    selectedCityId = ""
                    selectedCityLabel = ""
                    binding!!.tvFilterCity.setText("")
                    arrayListCity.clear()

                    getState(requireActivity(), null)

                    selectedInstitutionItems.clear()
                    selectedInstitutionId.clear()
                    selectedInstitutionLabel.clear()
                    arrayListInstitution.clear()
                    getInstitution(requireActivity(),"")
                    setDropDownInstitution()
                    binding!!.tvFilterInstitute.text=""
                }

            })
            recyclerView.adapter = adapter
            val getSavedSelectedCountryItemsId = getSavedSelectedCountryItem(AppConstants.CountryList)

            adapter.setInitialSelection(selectedCountryItems)
            adapter.onItemClickListener = { selectedCountry ->


                val selectedCountryLabels = selectedCountry?.label ?: "Default Label"
                val selectedCountryIds = selectedCountry?.value ?: "Default ID"

                binding!!.tvFilterCountry.text = selectedCountry?.label
                selectedCountryItems.label = selectedCountry?.label ?: ""
                selectedCountryItems.value = selectedCountry?.value ?: 0


                selectedCountryId = ""
                selectedCountryLabel = ""
                selectedCountryLabel = selectedCountry?.label ?: ""





                selectedCountryId = selectedCountryIds.toString()


            }

            popupWindow.showAsDropDown(binding!!.tvFilterCountry)

            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        adapter.getFilter().filter(s.toString())
                    }
                })
        }
    }

    private fun setDropDownInstitution() {
        binding!!.tvFilterInstitute.setOnClickListener {

            if (selectedCountryItems.label.isEmpty() || selectedCountryItems.value.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please select a country first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution here
            }



            val popupWindow = PopupWindow(requireActivity())
            val layout: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout
            layout.findViewById<TextView>(R.id.etSelect).hint = "Search Institution"

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterInstitute.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterFilterInstitutionSelector(requireActivity(), arrayListInstitution, layout)
            recyclerView.adapter = adapter

            val getSavedSelectedInstituionItemsId = getSavedSelectedInstitutionItems(AppConstants.institutionList)
            adapter.setInitialSelection(selectedInstitutionItems)



            adapter.onItemClickListener = { selectedInstitution ->
                val selectedInstitutionLabels = selectedInstitution.map { it.label }
                val selectedInstitutionIds = selectedInstitution.map { it.value.toString() }
                    .distinct() // Ensure unique IDs

                binding!!.tvFilterInstitute.text = selectedInstitution.joinToString("\n") { it.label }

                // Save selected items, ensuring no duplicates
                selectedInstitutionItems = selectedInstitution.distinct().toMutableList()

                // Clear the current selectedInstitutionId and add only the current selections
                selectedInstitutionId.clear()
                selectedInstitutionLabel.clear()



                selectedInstitutionIds.forEach { id ->
                    if (!selectedInstitutionId.contains(id)) {
                        selectedInstitutionId.add(id)
                    }
                }
                selectedInstitutionLabels.forEach { label ->
                    if (!selectedInstitutionLabel.contains(label)) {
                        selectedInstitutionLabel.add(label)
                    }
                }
                val currentSelectedlabels = selectedInstitution.map { it.label}
                selectedInstitutionLabel.retainAll(currentSelectedlabels)

                // Remove deselected IDs
                val currentSelectedIds = selectedInstitution.map { it.value.toString() }
                selectedInstitutionId.retainAll(currentSelectedIds)


            }

            popupWindow.showAsDropDown(binding!!.tvFilterInstitute)

            layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    adapter.getFilter().filter(s.toString())
                }
            })
        }
    }


    private fun setDropDownStudyLevel() {
        binding?.tvFilterStudyLevel?.setOnClickListener { v: View ->
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.findViewById<TextView>(R.id.etSelect).hint = "Search Campus"

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterStudyLevel.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            binding?.tvFilterStudyLevel?.getLocationOnScreen(locationOnScreen)
            val xLocationOfView = locationOnScreen[0]
            val yLocationOfView = locationOnScreen[1] + binding?.tvFilterStudyLevel?.height!!
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterFilterStudyLevelSelector(requireActivity(), arrayListStudyLevel, layout)
            recyclerView.adapter = adapter
            val getSavedSelectedItemsId = getSavedSelectedItems(AppConstants.studyLevelList)


            adapter.setInitialSelection(selectedStudyLevelItems)

            adapter.onItemClickListener = { selectedStudyLevel ->
                val selectedStudyLevelLabels = selectedStudyLevel.map { it.label }
                val selectedStudyLevelIds = selectedStudyLevel.map { it.value.toString() }
                    .distinct() // Ensure unique IDs

                binding!!.tvFilterStudyLevel.text =
                    selectedStudyLevel.joinToString("\n") { it.label }

                // Save selected items, ensuring no duplicates
                selectedStudyLevelItems = selectedStudyLevel.distinct().toMutableList()

                selectedStudyLevelId.clear()
                selectedStudyLevelLabel.clear()

                // Add new IDs, ensuring no duplicates
                selectedStudyLevelIds.forEach { id ->
                    if (!selectedStudyLevelId.contains(id)) {
                        selectedStudyLevelId.add(id)

                    }
                }

                selectedStudyLevelLabels.forEach { label ->
                    if (!selectedStudyLevelLabel.contains(label)) {
                        selectedStudyLevelLabel.add(label)
                    }
                }

                val currentSelectedLabel = selectedStudyLevel.map { it.label.toString() }
                selectedStudyLevelLabel.retainAll(currentSelectedLabel)

                // Remove deselected IDs
                val currentSelectedIds = selectedStudyLevel.map { it.value.toString() }
                selectedStudyLevelId.retainAll(currentSelectedIds)


            }

            popupWindow.showAsDropDown(binding!!.tvFilterStudyLevel)

            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        adapter.getFilter().filter(s.toString())
                    }
                })
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDownDiscipline() {
        binding?.tvFilterLookingFor?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Discipline")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterLookingFor.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            binding?.tvFilterLookingFor?.getLocationOnScreen(locationOnScreen)
            val xLocationOfView = locationOnScreen[0]
            val yLocationOfView = locationOnScreen[1] + binding?.tvFilterLookingFor?.height!!

            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterFilterDisplineSelector(requireActivity(), arrayListDiscipline, layout)
            recyclerView.adapter = adapter

            val getSavedSelectedDisciplineItemsId = getSavedDisplineItems(AppConstants.disciplineList)


            adapter.setInitialSelection(selectedDisciplineItems)


            adapter.onItemClickListener = { selectedDiscipline ->
                val selectedDisciplineLabels = selectedDiscipline.map { it.label.toString() }
                val selectedDisciplineIds = selectedDiscipline.map { it.value.toString() }
                    .distinct() // Ensure unique IDs

                binding!!.tvFilterLookingFor.text = selectedDiscipline.joinToString("\n") { it.label }

                // Save selected items, ensuring no duplicates
                selectedDisciplineItems = selectedDiscipline.distinct().toMutableList()

                selectedDisciplineId.clear()
                selectedDisciplineLabel.clear()

                selectedDisciplineIds.forEach { id ->
                    if (!selectedDisciplineId.contains(id)) {
                        selectedDisciplineId.add(id)
                    }
                }

                selectedDisciplineLabels.forEach { label ->
                    if (!selectedDisciplineLabel.contains(label)) {
                        selectedDisciplineLabel.add(label)
                    }
                }

                // Remove deselected IDs
                val currentSelectedIds = selectedDiscipline.map { it.value.toString() }
                selectedDisciplineId.retainAll(currentSelectedIds)

                val currentSelectedLabels = selectedDiscipline.map { it.label.toString() }
                selectedDisciplineLabel.retainAll(currentSelectedLabels)


            }

            popupWindow.showAsDropDown(binding!!.tvFilterLookingFor)

            layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    adapter.getFilter().filter(s.toString())
                }
            })
        }
    }





    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDownIntake() {
        binding?.tvFilterIntakeSelector?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Intake")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterIntakeSelector.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                binding?.tvFilterIntakeSelector?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvFilterIntakeSelector?.height!!
            } else {
                // If not in a fragment, use activity view for positioning
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvFilterIntakeSelector?.height!!
            }


            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterFilterIntakeSelector(requireActivity(),arrayListIntake, layout)
            recyclerView.adapter = adapter

            val getSavedSelectedIntakeItemsId = getSavedIntakeItems(AppConstants.IntakeList)


            adapter.setInitialSelection(selectedIntakeItems)


            adapter.onItemClickListener = { selectedIntake ->
                val selectedintakeLabels = selectedIntake.map { it.label}
                val selectedInatkeIds = selectedIntake.map { it.value.toString() }
                    .distinct() // Ensure unique IDs

                binding!!.tvFilterIntakeSelector.text = selectedIntake.joinToString("\n") { it.label }

                // Save selected items, ensuring no duplicates
                selectedIntakeItems = selectedIntake.distinct().toMutableList()

                selectedIntakeId.clear()
                selectedIntakeLabel.clear()

                selectedInatkeIds.forEach { id ->
                    if (!selectedIntakeId.contains(id)) {
                        selectedIntakeId.add(id)
                    }
                }
                selectedintakeLabels.forEach { label ->
                    if (!selectedIntakeLabel.contains(label)) {
                        selectedIntakeLabel.add(label)
                    }
                }

                // Remove deselected IDs
                val currentSelectedIds = selectedIntake.map { it.value.toString() }
                selectedIntakeId.retainAll(currentSelectedIds)

                val currentSelectedLabel = selectedIntake.map { it.label }
                selectedIntakeLabel.retainAll(currentSelectedLabel)



            }

            popupWindow.showAtLocation(binding!!.tvFilterIntakeSelector,Gravity.CENTER,0,-500)

            layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    adapter.getFilter().filter(s.toString())
                }
            })
        }
    }


    private fun getCountry(
        requireActivity: FragmentActivity,

        ) {

        ViewModalClass().getCountryModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { countryResponseModal: CountryResponse? ->
            countryResponseModal?.let { nonNullCountryResponseModal ->
                if (countryResponseModal.statusCode == 200) {

                    if(countryResponseModal.data != null) {
                        for (i in 0 until countryResponseModal!!.data!!.size) {
                            arrayListCountry?.add(countryResponseModal.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve countries. Please try again.");
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        countryResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }

    private fun getInstitution(
        requireActivity: FragmentActivity,
        selectedCountryId: String,

        ) {

        ViewModalClass().getInstitutionModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            selectedCountryId
        ).observe(requireActivity) { InstitutionModel: InstitutionModel? ->
            InstitutionModel?.let { nonNullInstitutionModel ->
                if (InstitutionModel.statusCode == 200) {

                    if(InstitutionModel.data != null) {
                        for (i in 0 until InstitutionModel!!.data!!.recordsInfo.size) {
                            arrayListInstitution?.add(InstitutionModel.data!!.recordsInfo[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve countries. Please try again.");
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        InstitutionModel.message ?: " Failed"
                    )
                }
            }
        }

    }



    private fun getStudyLevel(
        requireActivity: FragmentActivity

    ) {

        ViewModalClass().getStudyLevelModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(requireActivity) { studyLevelResponseModal: StudyLevelModal? ->
            studyLevelResponseModal?.let { nonNullCampusResponseModal ->
                if (studyLevelResponseModal.statusCode == 200) {

                    if(studyLevelResponseModal.data != null) {
                        for (i in 0 until studyLevelResponseModal!!.data!!.size) {
                            arrayListStudyLevel?.add(studyLevelResponseModal.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve StudyLevel. Please try again.");
                    }


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        studyLevelResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }



    private fun getState(
        requireActivity: FragmentActivity,
        countryId:Int?
        ) {

        ViewModalClass().getStateModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,countryId
        ).observe(requireActivity) { stateModelModal: stateModel? ->
            stateModelModal?.let { stateModelModal ->
                if (stateModelModal.statusCode == 200) {

                    if(stateModelModal.data != null) {
                        for (i in 0 until stateModelModal!!.data!!.size) {
                            arrayListState?.add(stateModelModal.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve countries. Please try again.");
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        stateModelModal.message ?: " Failed"
                    )
                }
            }
        }

    }


    private fun getCity(
        requireActivity: FragmentActivity,
        stateId:Int?
    ) {

        ViewModalClass().getCityModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,stateId
        ).observe(requireActivity) { CityModelModal:CityModel ? ->
            CityModelModal?.let { CityModel ->
                if (CityModelModal.statusCode == 200) {

                    if(CityModelModal.data != null) {
                        for (i in 0 until CityModelModal!!.data!!.size) {
                            arrayListCity?.add(CityModelModal.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve countries. Please try again.");
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        CityModelModal.message ?: " Failed"
                    )
                }
            }
        }

    }


    private fun getDisciplineLevel(
        requireActivity: FragmentActivity

    ) {

        ViewModalClass().getDisciplineModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(requireActivity) { displineResponseModal: DisciplineModel? ->
            displineResponseModal?.let { nonNullCampusResponseModal ->
                if (displineResponseModal.statusCode == 200) {

                    if(displineResponseModal.data != null) {
                        for (i in 0 until displineResponseModal!!.data!!.size) {
                            arrayListDiscipline?.add(displineResponseModal.data!![i])

                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve StudyLevel. Please try again.");
                    }



                } else {
                    CommonUtils.toast(
                        requireActivity,
                        displineResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }



    private fun getIntake(
        requireActivity: FragmentActivity) {

        ViewModalClass().getIntakeModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(requireActivity) { intakeModelResponseModal: IntakeModel? ->
            intakeModelResponseModal?.let { nonNullCampusResponseModal ->
                if (intakeModelResponseModal.statusCode == 200) {

                    if(intakeModelResponseModal.data != null) {
                        for (i in 0 until intakeModelResponseModal!!.data!!.size) {
                            arrayListIntake?.add(intakeModelResponseModal.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve testScore. Please try again.");
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        intakeModelResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }

    private fun saveSelectedValuesToSharedPreferences(
        keyPrefix: String,
        ids: List<String>,
        labels: List<String>
    ) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.putStringList("${keyPrefix}Id", ids)
        sharedPrefs.putStringList("${keyPrefix}Label", labels)
    }

    private fun getSavedSelectedItemsId(keyPrefix: String): List<String> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()

        return ids // Adjust this according to your data format
    }
    private fun getSavedSelectedItemsLabel(keyPrefix: String): List<String> {
        val sharedPrefs = SharedPrefs(requireContext())
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        return labels // Adjust this according to your data format
    }

    private fun saveSelectedToSharedPreferences(
        keyPrefix: String,
        ids: String,
        labels:String
    ) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }

    private fun getSavedSelectedItemId(keyPrefix: String): String {
        val sharedPrefs = SharedPrefs(requireContext())
        // Get the single string from SharedPrefs
        val id = sharedPrefs.getString("${keyPrefix}Id", "") ?: ""

        return id
    }
    private fun getSavedSelectedItemLabel(keyPrefix: String): String {
        val sharedPrefs = SharedPrefs(requireContext())
        // Get the single string from SharedPrefs
        val label = sharedPrefs.getString("${keyPrefix}Label", "") ?: ""

        return label
    }
    private fun getSavedSelectedItems(keyPrefix: String): List<Data> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                Data(label = label, value = id)
            } else {
                null
            }
        }
    }
    private fun getSavedSelectedInstitutionItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.institutionModel.RecordsInfo(label = label, value = id)
            } else {
                null
            }
        }
    }

    private fun getSavedSelectedCountryItem(keyPrefix: String): com.student.Compass_Abroad.modal.countryModel.DataX? {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure there's at least one ID and one label
        if (ids.isNotEmpty() && labels.isNotEmpty()) {
            val id = ids.firstOrNull()?.toIntOrNull()
            val label = labels.firstOrNull()
            if (id != null && label != null) {
                return com.student.Compass_Abroad.modal.countryModel.DataX(label = label, value = id)
            }
        }
        // Return null if no valid data is found
        return null
    }






    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDownState() {
        binding!!.tvFilterState.setOnClickListener {

            if (selectedCountryItems.label.isEmpty() || selectedCountryItems.value.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please select a country first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution here
            }


            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout
            layout.findViewById<TextView>(R.id.etSelect).hint = "Search State"

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true

            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterState.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())

            // Using AdapterFilterCollegeStateSelector for the state dropdown (create this adapter similar to AdapterFilterCollegeCountrySelector)
            val adapter =
                AdapterFilterCollegeStateSelector(requireActivity(), arrayListState, layout)
            recyclerView.adapter = adapter

            // Restore previously selected items
            val getSavedSelectedStateItemsId = getSavedSelectedStateItem(AppConstants.StateList)
            adapter.setInitialSelection(selectedStateItems1)

            adapter.onItemClickListener = { selectedState ->

                val selectedStateLabels = selectedState?.label ?: "Default Label"
                val selectedStateIds = selectedState?.value ?: "Default ID"

                binding!!.tvFilterState.text = selectedState?.label
                selectedStateItems1.label = selectedState?.label ?: ""
                selectedStateItems1.value = selectedState?.value ?: 0

                // Save selected items
                selectedStateItems = selectedState?.label ?: ""

                selectedStateId = ""
                selectedStateLabel = ""
                selectedStateLabel = selectedState?.label ?: ""


                clearSelectedValuesFromSharedPreferences(AppConstants.CityList)
                selectedCityItems1.label = ""
                selectedCityItems1.value = 0
                selectedCityItems = ""
                selectedCityId = ""
                selectedCityLabel = ""
                binding!!.tvFilterCity.text = ""
                arrayListCity.clear()


                clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
                selectedInstitutionItems.clear()
                selectedInstitutionId.clear()
                selectedInstitutionLabel.clear()
                binding!!.tvFilterInstitute.setText("")
                arrayListInstitution.clear()


                getCity(requireActivity(),selectedState?.value  )

                selectedStateId = selectedStateIds.toString()

            }

            popupWindow.showAsDropDown(binding!!.tvFilterState)

            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        adapter.getFilter().filter(s.toString())
                    }
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDownCity() {
        binding!!.tvFilterCity.setOnClickListener {

            if (selectedStateItems1.label.isEmpty() || selectedStateItems1.value.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please select a State first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution here
            }

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout
            layout.findViewById<TextView>(R.id.etSelect).hint = "Search City"

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvFilterCity.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterFilterCollegeCitySelector(requireActivity(), arrayListCity, layout)
            recyclerView.adapter = adapter
            val getSavedSelectedCityItemsId = getSavedSelectedCityItem(AppConstants.CityList)
            adapter.setInitialSelection(selectedCityItems1)

            adapter.onItemClickListener = { selectedCity ->

                val selectedCityLabels = selectedCity?.label ?: "Default Label"
                val selectedCityIds = selectedCity?.value ?: "Default ID"

                binding!!.tvFilterCity.text = selectedCity?.label

                // Save selected items
                selectedCityItems = selectedCity?.label ?: ""

                selectedCityItems1.label = selectedCity?.label ?: ""
                selectedCityItems1.value = selectedCity?.value ?: 0

                selectedCityId = ""
                selectedCityLabel = ""
                selectedCityLabel = selectedCity?.label ?: ""


                clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
                selectedInstitutionItems.clear()
                selectedInstitutionId.clear()
                selectedInstitutionLabel.clear()
                binding!!.tvFilterInstitute.setText("")
                arrayListInstitution.clear()


                selectedCityId = selectedCityIds.toString()

            }

            popupWindow.showAsDropDown(binding!!.tvFilterCity)

            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        adapter.getFilter().filter(s.toString())
                    }
                })
        }
    }



    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }
    private fun clearAllSelectedValues() {
        // Call this method for each key prefix you use
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.StateList)
        clearSelectedValuesFromSharedPreferences(AppConstants.CityList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }

    private fun getSavedSelectedStateItem(keyPrefix: String): com.student.Compass_Abroad.modal.stateModel.Data? {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure there's at least one ID and one label
        if (ids.isNotEmpty() && labels.isNotEmpty()) {
            val id = ids.firstOrNull()?.toIntOrNull()
            val label = labels.firstOrNull()
            if (id != null && label != null) {
                return com.student.Compass_Abroad.modal.stateModel.Data(
                    label = label,
                    value = id
                )
            }
        }
        // Return null if no valid data is found
        return null
    }

    private fun getSavedSelectedCityItem(keyPrefix: String): com.student.Compass_Abroad.modal.cityModel.Data? {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure there's at least one ID and one label
        if (ids.isNotEmpty() && labels.isNotEmpty()) {
            val id = ids.firstOrNull()?.toIntOrNull()
            val label = labels.firstOrNull()
            if (id != null && label != null) {
                return com.student.Compass_Abroad.modal.cityModel.Data(
                    label = label,
                    value = id
                )
            }
        }
        // Return null if no valid data is found
        return null
    }


    private fun saveValues(
        minTutionFee: String,
        maxTutionFee: String,
        minApplicationFee: String,
        maxApllicationFee: String
    ) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.putString11(AppConstants.PGWP_KEY, PGWP)
        sharedPrefs.putString11(AppConstants.ATTENDANCE_KEY, Attendance)
        sharedPrefs.putString11(AppConstants.PROGRAM_TYPE_KEY, ProgramType)
        sharedPrefs.putString11(AppConstants.Accomodation, accomodation)
        sharedPrefs.putString11(AppConstants.MIN_TUTION_KEY, minTutionFee)
        sharedPrefs.putString11(AppConstants.MAX_TUTION_KEY, maxTutionFee)
        sharedPrefs.putString11(AppConstants.MIN_APPLICATION_KEY, minApplicationFee)
        sharedPrefs.putString11(AppConstants.MAX_APPLICATION_KEY, maxApllicationFee)
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible=false
    }
    private fun getSavedDisplineItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.getProgramFilters.Discipline> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.getProgramFilters.Discipline(
                    label = label,
                    value = id
                )
            } else {
                null
            }
        }
    }

    private fun getSavedIntakeItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.getProgramFilters.Intake> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.getProgramFilters.Intake(label = label, value = id)
            } else {
                null
            }
        }
    }

    private fun getSavedStudyLevelItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.studyLevelModel.Data> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.studyLevelModel.Data(
                    label = label,
                    value = id
                )
            } else {
                null
            }
        }
    }

    private fun getSavedDisciplineItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.discipline.Data> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.discipline.Data(
                    label = label,
                    value = id
                )
            } else {
                null
            }
        }
    }


    private fun getSavedIntakesItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.intakeModel.Data> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.intakeModel.Data(label = label, value = id)
            } else {
                null
            }
        }
    }



    private fun getSavedInstutionItems(keyPrefix: String): List<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()
        val labels = sharedPrefs.getStringList("${keyPrefix}Label") ?: emptyList()

        // Ensure ids and labels have the same size
        val minSize = minOf(ids.size, labels.size)

        // Create a list of Data objects combining ids and labels
        return (0 until minSize).mapNotNull { index ->
            val id = ids.getOrNull(index)?.toIntOrNull()
            val label = labels.getOrNull(index)
            if (id != null && label != null) {
                com.student.Compass_Abroad.modal.institutionModel.RecordsInfo(
                    label = label,
                    value = id
                )
            } else {
                null
            }
        }
    }
}