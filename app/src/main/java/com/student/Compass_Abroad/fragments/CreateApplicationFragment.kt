package com.student.Compass_Abroad.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.CreateApplicationRequest
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterDestinationCountrySelector
import com.student.Compass_Abroad.adaptor.AdapterGetCampusList
import com.student.Compass_Abroad.adaptor.AdapterGetIntakeList
import com.student.Compass_Abroad.adaptor.AdapterGetPreferCourseList
import com.student.Compass_Abroad.adaptor.AdapterPreferCollageList
import com.student.Compass_Abroad.databinding.FragmentCreateApplicationBinding
import com.student.Compass_Abroad.fragments.home.SharedViewModel
import com.student.Compass_Abroad.modal.CreateApplication.CreateApplicationModal
import com.student.Compass_Abroad.modal.GetCampusModal.GetCampusResponse
import com.student.Compass_Abroad.modal.PreferCollageModal.PreferCollageModal
import com.student.Compass_Abroad.modal.PreferCollageModal.RecordsInfo
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.intakeModel.IntakeModel
import com.student.Compass_Abroad.retrofit.ViewModalClass


class  CreateApplicationFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateApplicationBinding
    private val arrayListCountry: MutableList<com.student.Compass_Abroad.modal.getDestinationCountryList.Data> =
        mutableListOf()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val arrayListCampus: MutableList<com.student.Compass_Abroad.modal.GetCampusModal.Data> =
        mutableListOf()
    var arrayListIntake = ArrayList<com.student.Compass_Abroad.modal.intakeModel.Data>()
    private val arrayListPreferCollage: MutableList<RecordsInfo> = mutableListOf()
    var destinationCountry: String? = null
    var collageId: String? = null
    private var courseId: String? = null
    var country_id: String? = null
    var selected_year: String? = null
    var prefer_course_id: String? = null
    var intake_id: String? = null
    private val selectedCourses: MutableList<com.student.Compass_Abroad.modal.GetCampusModal.Data> = mutableListOf()
    private var previouslySelectedCampusId: String? = null
    private var previouslySelectedCountryId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateApplicationBinding.inflate(inflater, container, false)

        getDestinationCountryList(requireActivity(), binding.destinationCountry)

        selectYears()

        binding.ivHeader.apply {
            alpha = 0f
            scaleX = 0.8f
            scaleY = 0.8f

            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setInterpolator(android.view.animation.DecelerateInterpolator())
                .start()
        }

        onClicks()
        return binding.root
    }




    private fun onClicks() {
        binding.btnCreateApplication.setOnClickListener {
            when {
                country_id.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a country")
                }

                collageId.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a college")
                }

                courseId.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a campus")
                }

                prefer_course_id.isNullOrEmpty() -> {
                    CommonUtils.toast(requireActivity(), "Please select a preferred course")
                }
                prefer_course_id == "[]" -> {
                    CommonUtils.toast(requireActivity(), "Please select a preferred course")
                }

                binding.selectYear.selectedItemPosition == 0 -> {
                    CommonUtils.toast(requireActivity(), "Please select a year")
                }

                intake_id.isNullOrEmpty() -> {
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

        binding.destinationCountry.setOnClickListener {
            getDestinationCountryList(requireActivity(), binding.destinationCountry)

        }


        binding.preferCollage.setOnClickListener {
            getPreferCollage(requireActivity(), binding.preferCollage)

        }

        binding.selectCampus.setOnClickListener {
            getCampusList(requireActivity(), binding.selectCampus)

        }
        binding.preferCourse.setOnClickListener {
            getPreferCourseList(requireActivity(), binding.preferCourse)

        }

        binding.selectIntake.setOnClickListener {
            getSelectIntakeList(requireActivity(), binding.selectIntake)

        }

    }

    private fun createApplication() {
        val programsArray = if (prefer_course_id!!.startsWith("[") && prefer_course_id!!.endsWith("]")) {
            prefer_course_id!!
                .removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().toIntOrNull() }
                .filterNotNull()
                .filter { it > 0 }
        } else {
            listOfNotNull(prefer_course_id!!.toIntOrNull()) // Convert single item to an integer if valid
        }

        val requestBody = CreateApplicationRequest(

            intake = intake_id!!.toInt(),
            intake_year = selected_year!!.toInt(),
            institution = collageId?.toInt() ?: 0,
            destination_country = country_id?.toInt() ?: 0,
            campus = courseId?.toInt() ?: 0,
            lead_identifier = App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER, "")!!,
            programs = programsArray

        )

        ViewModalClass().createApplicationMutableData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            requestBody // Pass the data class instance here
        ).observe(requireActivity()) { destination: CreateApplicationModal? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 201) {
                    App.singleton?.createApplicationIdentifier = getDestinationCountry.data?.applicationInfo?.identifier

                    val bundle = Bundle()
                    val status = "1"
                    bundle.putString("status", status)
                    Navigation.findNavController(binding.root).navigate(R.id.uploadProgramDocFragment, bundle)

                    sharedViewModel.triggerRefreshData()
                } else {
                    CommonUtils.toast(requireActivity(), getDestinationCountry.message ?: "Failed")
                }
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
                        CommonUtils.toast(requireActivity, "Failed to retrieve destinationCountry. Please try again.")
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

    private fun selectYears() {
        val years = arrayListOf("Select Year", "2025", "2026", "2027", "2028", "2029", "2030")

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            years
        ) {
            @SuppressLint("ResourceAsColor")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.parseColor("#86878B") else Color.BLACK)
                return view
            }

            @SuppressLint("ResourceAsColor")
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.parseColor("#86878B") else Color.BLACK)
                return view
            }
        }

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
                // Optional: handle no selection
            }
        }
    }


    private fun getPreferCourseList(requireActivity: FragmentActivity, preferCourse: TextView) {
        if (courseId.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select campus first")
            return
        }
        arrayListCampus.clear()
        // selectedCourses.clear() // Clear the selected courses list
      //  preferCourse.text = "" // Clear the TextView

        collageId?.let {
            ViewModalClass().getPreferCourseData(
                requireActivity,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer " + CommonUtils.accessToken, courseId!!, it
            ).observe(requireActivity) { destination: GetCampusResponse? ->
                destination?.let { getDestinationCountry ->
                    if (getDestinationCountry.statusCode == 200) {
                        getDestinationCountry.data?.let {
                            arrayListCampus.addAll(it)
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
        val xLocationOfView = locationOnScreen[0]
        val yLocationOfView = locationOnScreen[1] + preferCourse.height

        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter =
            AdapterGetPreferCourseList(requireActivity(), arrayListCampus, layout, selectedCourses)
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
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val enteredText = s.toString()
                    adapter.getFilter().filter(enteredText)
                }
            })
    }

    private fun getCampusList(requireActivity: FragmentActivity, selectCampus: TextView) {
        if (collageId.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select prefer collage first")
            return
        }

        // Clear the previous campus list and related fields
        arrayListCampus.clear()


        ViewModalClass().getCampusListData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, collageId!!
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
            destinationCountry = selectedCountry.value.toString()
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
    private fun clearRelatedFields() {
        binding.preferCourse.text = ""
        binding.selectIntake.text = ""
        // Clear the text of the select year Spinner
        binding.selectYear.setSelection(0)
        collageId = ""
        courseId = ""
        prefer_course_id = ""
    }
    private fun getPreferCollage(requireActivity: FragmentActivity, preferCollage: TextView) {
        if (country_id.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select country first")
            return
        }

        arrayListPreferCollage.clear()
        ViewModalClass().getPreferCollageData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, country_id!!
        ).observe(requireActivity) { destination: PreferCollageModal? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    getDestinationCountry.data?.recordsInfo?.let {
                        arrayListPreferCollage.addAll(it)
                    } ?: run {
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setDropDownPreferCollageList(preferCollage)
                } else {
                    CommonUtils.toast(requireActivity, getDestinationCountry.message ?: "Failed")
                }
            }
        }
    }
    private fun setDropDownPreferCollageList(preferCollage: TextView) {
        val popupWindow = PopupWindow(requireActivity())
        val layout: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
        popupWindow.contentView = layout

        layout.findViewById<TextView>(R.id.etSelect).setHint("Search Collage")

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.elevation = 5f
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.width = preferCollage.width

        val locationOnScreen = IntArray(2)
        preferCollage.getLocationOnScreen(locationOnScreen)

        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterPreferCollageList(requireActivity(), arrayListPreferCollage, layout)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { selectedCollage ->
            preferCollage.text = selectedCollage.label
            collageId = selectedCollage.value.toString()

            arrayListCampus.clear()
            binding.selectCampus.text = ""
            binding.preferCourse.text = ""

            popupWindow.dismiss()
            // You can perform additional actions based on the selected collage here
        }

        popupWindow.showAsDropDown(preferCollage)

        layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val enteredText = s.toString()
                adapter.getFilter().filter(enteredText)
            }
        })
    }

    private fun getDestinationCountryList(requireActivity: FragmentActivity, et_destination_Country: TextView) {
        ViewModalClass().getDestinationCountryLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: getDestinationCountry? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    getDestinationCountry.data?.let {
                        arrayListCountry.addAll(it)
                    } ?: run {
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setDropDownDestinationCountryList(et_destination_Country)
                } else {
                    CommonUtils.toast(requireActivity, getDestinationCountry.message ?: "Failed")
                }
            }
        }
    }
    private fun setDropDownDestinationCountryList(et_destination_Country: TextView) {
        et_destination_Country.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.findViewById<TextView>(R.id.etSelect).setHint("Search Country")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = et_destination_Country.width

            val locationOnScreen = IntArray(2)
            et_destination_Country.getLocationOnScreen(locationOnScreen)
            val xLocationOfView = locationOnScreen[0]
            val yLocationOfView = locationOnScreen[1] + et_destination_Country.height

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterDestinationCountrySelector(requireActivity(), arrayListCountry, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                et_destination_Country.text = selectedCountry.label
                country_id = selectedCountry.value.toString()

                // Clear the preferred college, campus, and selected courses lists when a new country is selected
                if (previouslySelectedCountryId != country_id) {
                    arrayListPreferCollage.clear()
                    binding.preferCollage.text = ""
                    arrayListCampus.clear()
                    binding.selectCampus.text = ""
                    selectedCourses.clear()
                    binding.preferCourse.text = ""
                    clearRelatedFields()
                }

                previouslySelectedCountryId = country_id

                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }

            popupWindow.showAsDropDown(et_destination_Country)

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
                        val enteredText = s.toString()
                        adapter.getFilter().filter(enteredText)
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

    }

}