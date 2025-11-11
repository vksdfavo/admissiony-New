package com.student.Compass_Abroad.fragments.program

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.CreateApplicationRequest
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterCountryList
import com.student.Compass_Abroad.adaptor.AdapterGetCampusList
import com.student.Compass_Abroad.adaptor.AdapterGetIntakeList
import com.student.Compass_Abroad.adaptor.AdapterGetPreferCourseList
import com.student.Compass_Abroad.databinding.FragmentApplyProgramBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.home.FragProgramDetailDetails
import com.student.Compass_Abroad.fragments.home.ProgramDetails
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.CreateApplication.CreateApplicationModal
import com.student.Compass_Abroad.modal.GetCampusModal.Data
import com.student.Compass_Abroad.modal.GetCampusModal.GetCampusResponse
import com.student.Compass_Abroad.modal.GetStudentsModal.GetStudentResponse
import com.student.Compass_Abroad.modal.intakeModel.IntakeModel
import com.student.Compass_Abroad.retrofit.ViewModalClass

class ApplyProgramFragment : BaseFragment() {
    private lateinit var binding: FragmentApplyProgramBinding
    private var prefer_course_id: String = ""
    private var intake_id: String = ""
    private val arrayListCampus: MutableList<Data> = mutableListOf()
    private val arrayListCourses: MutableList<Data> = mutableListOf()
    private val selectedCourses: MutableList<Data> = mutableListOf()
    var arrayListIntake = ArrayList<com.student.Compass_Abroad.modal.intakeModel.Data>()
    private var selected_year: String = ""
    private var campus_id: String = ""
    private var collage_id: String = ""
    private var courseId: String? = null
    private var previouslySelectedCampusId: String? = null
    private val arrayListStudents: MutableList<com.student.Compass_Abroad.modal.GetStudentsModal.Data> =
        mutableListOf()

    companion object {
        var details: Record? = null
    }
    private var fragment: Fragment? = null
    private var xLocationOfView = 0
    private var yLocationOfView = 0
    var lead_identifier: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyProgramBinding.inflate(inflater, container, false)

        onClicks()
        selectYears()
        setData()


        lead_identifier = App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER,"").toString()
        return binding.root
    }

    private fun setData() {
        binding.destinationCountry.text = FragProgramDetailDetails.details?.program?.institution?.country?.name
        binding.preferCollage.text = FragProgramDetailDetails.details?.program?.institution?.name
        collage_id = FragProgramDetailDetails.details?.program?.institution_id?.toString()?:""
    }

    private fun onClicks() {

        binding.btnCreateApplication.setOnClickListener {

            Log.d("lead_identifier", lead_identifier)
            when {

                lead_identifier.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a student")
                }

                courseId.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a campus")
                }

                prefer_course_id.isEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a preferred course")
                }


                prefer_course_id == "[]" -> {
                    CommonUtils.toast(requireActivity(), "Please select a preferred course")
                }
                binding.selectYear.selectedItemPosition == 0 -> {
                    CommonUtils.toast(requireActivity(), "Please select a year")
                }
                intake_id.isEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select an intake")
                }
                else -> {

                    createApplication()
                }
            }
        }


        binding.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        binding.selectIntake.setOnClickListener {

            getSelectIntakeList(requireActivity(), binding.selectIntake)
        }

        binding.selectCampus.setOnClickListener {

            getCampusList(requireActivity(), binding.selectCampus)
        }

        binding.preferCourse.setOnClickListener {
            getPreferCourseList(requireActivity(), binding.preferCourse)

        }
    }


    private fun getCampusList(requireActivity: FragmentActivity, selectCampus: TextView) {
        if (collage_id.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select prefer collage first")
            return
        }

        // Clear the previous campus list and related fields
        arrayListCampus.clear()


        ViewModalClass().getCampusListData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, collage_id!!
        ).observe(requireActivity) { destination: GetCampusResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    if (getDestinationCountry.data != null) {
                        for (i in 0 until getDestinationCountry.data.size) {
                            arrayListCampus.add(getDestinationCountry.data[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setGetCampusList(selectCampus)

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDestinationCountry.message ?: "Failed"
                    )
                }
            }
        }
    }
    private fun setGetCampusList(selectCampus: TextView) {
        val popupWindow = PopupWindow(requireActivity())
        val layout: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
        popupWindow.contentView = layout

        layout.findViewById<TextView>(R.id.etSelect).setHint("Search Campus")

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.elevation = 5f
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.width = selectCampus.width

        val locationOnScreen = IntArray(2)
        selectCampus.getLocationOnScreen(locationOnScreen)
        val xLocationOfView = locationOnScreen[0]
        val yLocationOfView = locationOnScreen[1] + selectCampus.height

        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterGetCampusList(requireActivity(), arrayListCampus, layout)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { selectedCountry ->
            selectCampus.text = selectedCountry.label
            //destinationCountry = selectedCountry.value.toString()
            courseId = selectedCountry.value.toString()

            // Clear the preferred course list and update the TextView
            if (previouslySelectedCampusId != courseId) {
                selectedCourses.clear()
                binding.preferCourse.text = ""
            }

            previouslySelectedCampusId = courseId

            popupWindow.dismiss()
            // You can perform additional actions based on the selected country here
        }

        popupWindow.showAsDropDown(selectCampus)

        layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val enteredText = s.toString()
                adapter.getFilter().filter(enteredText)
            }
        })
    }



    private fun getPreferCourseList(requireActivity: FragmentActivity, preferCourse: TextView) {
        if (courseId.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select campus first")
            return
        }
        arrayListCourses.clear()

        collage_id?.let {
            ViewModalClass().getPreferCourseData(
                requireActivity,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer " + CommonUtils.accessToken, courseId.toString(),it
            ).observe(requireActivity) { destination: GetCampusResponse? ->
                destination?.let { getDestinationCountry ->
                    if (getDestinationCountry.statusCode == 200) {
                        getDestinationCountry.data?.let { data ->
                            arrayListCourses.clear()
                            arrayListCourses.addAll(data)
                            Log.e("sjsjjs",arrayListCourses.toString())

                            /*// Set the first item in the TextView
                            if (arrayListCourses.isNotEmpty()) {
                                setFirstCourseItem(preferCourse, arrayListCourses, prefer_course_id)
                            }*/
                        } ?: run {
                            CommonUtils.toast(
                                requireActivity,
                                "Failed to retrieve destinationCountry. Please try again."
                            )
                        }
                        setGetPreferCourseList(preferCourse)
                    } else {
                        CommonUtils.toast(
                            requireActivity,
                            getDestinationCountry.message ?: "Failed"
                        )
                    }
                }
            }
        }
    }

    private fun setFirstCourseItem(
        preferCourse: TextView,
        arrayListCampus: MutableList<Data>,
        prefer_course_id: String
    ) {

        val matchingItem = arrayListCampus.find { it.value ==prefer_course_id.toInt() }

        val firstItem = matchingItem ?: arrayListCampus[0]

        if (!selectedCourses.contains(firstItem)) {
            selectedCourses.add(firstItem)
        }

        preferCourse.text = firstItem.label


        val courseIds = selectedCourses.map { it.value }
        this.prefer_course_id = courseIds.joinToString(prefix = "[", postfix = "]", separator = ",")
    }


    private fun setGetPreferCourseList(preferCourse: TextView) {
        val popupWindow = PopupWindow(requireActivity())
        val layout: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
        popupWindow.contentView = layout

        layout.findViewById<TextView>(R.id.etSelect).setHint("Search Campus")

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.elevation = 5f
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.width = preferCourse.width

        val locationOnScreen = IntArray(2)
        preferCourse.getLocationOnScreen(locationOnScreen)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterGetPreferCourseList(requireActivity(), arrayListCourses, layout, selectedCourses)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = {
            preferCourse.text = selectedCourses.joinToString("\n\n") { it.label }
            val courseIds = selectedCourses.map { it.value }
            prefer_course_id = courseIds.joinToString(prefix = "[", postfix = "]", separator = ",")
        }

        popupWindow.showAsDropDown(preferCourse)

        layout.findViewById<EditText>(R.id.etSelect)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                )
                {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val enteredText = s.toString()
                    adapter.getFilter().filter(enteredText)
                }
            })
    }


    private fun createApplication() {
        val programsArray = if (prefer_course_id.startsWith("[") && prefer_course_id.endsWith("]")) {
            prefer_course_id
                .removeSurrounding("[", "]")
                .split(",").mapNotNull { it.trim().toIntOrNull() }
                .filter { it > 0 }
        } else {
            listOfNotNull(prefer_course_id.toIntOrNull())
        }

        val requestBody = CreateApplicationRequest(
            intake = intake_id.toInt(),
            intake_year = selected_year.toInt(),
            institution = ProgramDetails.details?.program?.institution_id ?: 0,
            destination_country = ProgramDetails.details?.program?.institution?.country_id ?: 0,
            campus = courseId?.toInt() ?: 0,
            lead_identifier = lead_identifier,
            programs = programsArray
        )

        ViewModalClass().createApplicationMutableData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            requestBody
        ).observe(requireActivity()) { destination: CreateApplicationModal? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 201) {
                    App.singleton?.createApplicationIdentifier = getDestinationCountry.data?.applicationInfo?.identifier
                    Navigation.findNavController(binding.root).navigate(R.id.uploadProgramDocFragment)
                } else {
                    CommonUtils.toast(requireActivity(), getDestinationCountry.message ?: "Failed")
                }
            }
        }
    }

    private fun selectYears() {
        val years =
            arrayListOf("Select Year", "2025", "2026", "2027", "2028", "2029", "2030")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectYear.adapter = adapter

        binding.selectYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedYear = years[position]
                if (selectedYear != "Select Year") {

                    selected_year = selectedYear
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }
    }
    private fun getSelectIntakeList(requireActivity: FragmentActivity, selectIntake: TextView) {
        ViewModalClass().getIntakeModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: IntakeModel? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    if (getDestinationCountry.data != null) {
                        arrayListIntake.clear() // Clear the list before adding new items
                        arrayListIntake.addAll(getDestinationCountry.data!!)
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setGetIntakeDataList(selectIntake)

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDestinationCountry.message ?: "Failed"
                    )
                }
            }
        }
    }


    private fun setGetIntakeDataList(selectIntake: TextView) {
        val popupWindow = PopupWindow(requireActivity())
        val layout: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
        popupWindow.contentView = layout

        layout.findViewById<TextView>(R.id.etSelect).setHint("Search Intake")

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.elevation = 5f
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.width = selectIntake.width

        // Set recycler view adapter
        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterGetIntakeList(requireActivity(), arrayListIntake, layout)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { selectedCountry ->
            selectIntake.text = selectedCountry.label
            intake_id = selectedCountry.value.toString()
            popupWindow.dismiss()
            // You can perform additional actions based on the selected country here
        }

        // Calculate the center position of the screen
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val popupWidth = selectIntake.width
        val popupHeight = popupWindow.height

        val xOffset = (screenWidth - popupWidth) / 2
        val yOffset = (screenHeight - popupHeight) / 2

        // Show the popup window in the center of the screen
        popupWindow.showAtLocation(selectIntake, Gravity.NO_GRAVITY, xOffset, yOffset)

        // Search edit text
        layout.findViewById<EditText>(R.id.etSelect)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    val enteredText = s.toString()
                    adapter.getFilter().filter(enteredText)
                }
            })

    }
}