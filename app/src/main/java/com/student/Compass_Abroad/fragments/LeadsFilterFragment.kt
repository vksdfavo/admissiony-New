package com.student.Compass_Abroad.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterCountryList
import com.student.Compass_Abroad.adaptor.AdapterUtmSourceList
import com.student.Compass_Abroad.databinding.DialogSingleDatePickerBinding
import com.student.Compass_Abroad.databinding.FragmentLeadsFilterBinding
import com.student.Compass_Abroad.modal.GetStudentsModal.GetStudentResponse
import com.student.Compass_Abroad.modal.UtmModal.RecordsInfo
import com.student.Compass_Abroad.modal.UtmModal.UtmModalResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LeadsFilterFragment : BaseFragment() {
    private lateinit var binding: FragmentLeadsFilterBinding
    private val arraySourceCountryList: MutableList<com.student.Compass_Abroad.modal.GetStudentsModal.Data> =
        mutableListOf()
    private val arraySourceStateList: MutableList<com.student.Compass_Abroad.modal.GetStudentsModal.Data> =
        mutableListOf()
    private val arrayLeadDestinationCountryList: MutableList<com.student.Compass_Abroad.modal.GetStudentsModal.Data> =
        mutableListOf()
    private val arrayUtmSourceList: MutableList<RecordsInfo> = mutableListOf()
    private var fragment: Fragment? = null
    private var xLocationOfView = 0
    private var yLocationOfView = 0
    var sourceCountry: String = ""
    var sourceStateData: String = ""
    var destinationCountry: String = ""
    var has_document: String = ""
    var has_application: String = ""
    var has_counselling: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLeadsFilterBinding.inflate(inflater, container, false)

        getSourceCountryList(requireActivity(), binding.sourceCountry)
        getLeadDestinationCountry(requireActivity(), binding.destinationCountry)
        getUtmSourceData(requireActivity(), binding.utmSource)


        onClicks()
        setRadioButton()

        return binding.root
    }

    private fun setRadioButton() {
        binding.hasDocument.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_yes) {
                has_document = binding.radioYes.text.toString()
            } else if (checkedId == R.id.radio_no) {
                has_document = binding.radioNo.text.toString()

            }
        }
        binding.hasApplication.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.yes_application) {
                has_application = binding.yesApplication.text.toString()

            } else if (checkedId == R.id.no_application) {
                has_application = binding.noApplication.text.toString()

            }
        }
        binding.hasCounseling.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.yes_counseling) {
                has_counselling = binding.yesCounseling.text.toString()


            } else if (checkedId == R.id.no_counseling) {
                has_counselling = binding.noCounseling.text.toString()

            }
        }
    }
    private fun onClicks() {

        binding.fabAcBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        binding.sourceCountry.setOnClickListener {
            getSourceCountryList(requireActivity(), binding.sourceCountry)
        }

        binding.sourceState.setOnClickListener {
            getSourceStateList(requireActivity(), binding.sourceState)
        }

        binding.destinationCountry.setOnClickListener {

            getLeadDestinationCountry(requireActivity(), binding.destinationCountry)

        }
        binding.utmSource.setOnClickListener {

            getUtmSourceData(requireActivity(), binding.utmSource)

        }

        binding.createAt.setOnClickListener {

            showSingleDatePickerDialog(binding.createAt)
        }
        binding.convertedAt.setOnClickListener {

            showSingleDatePickerDialog(binding.convertedAt)
        }
    }
    private fun showSingleDatePickerDialog(textView: TextView) {
        val binding = DialogSingleDatePickerBinding.inflate(LayoutInflater.from(requireActivity()))
        val datePicker = binding.datePicker
        val buttonConfirm = binding.buttonConfirm

        val alertDialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setCancelable(false)
            .create()

        var startDate: Long? = null
        var endDate: Long? = null

        buttonConfirm.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)

            if (startDate == null) {
                startDate = calendar.timeInMillis
                buttonConfirm.text = "Confirm"
                Toast.makeText(requireActivity(), "Pick the end date", Toast.LENGTH_SHORT).show()
            } else {
                endDate = calendar.timeInMillis

                if (endDate!! <= startDate!!) {
                    Toast.makeText(
                        requireActivity(),
                        "End date must be after start date",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                val formattedStartDate = sdf.format(Date(startDate!!))
                val formattedEndDate = sdf.format(Date(endDate!!))

                textView.text = "$formattedStartDate – $formattedEndDate"
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
    private fun getUtmSourceData(requireActivity: FragmentActivity, utmSource: TextView) {
        ViewModalClass().getUtmSourceListData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: UtmModalResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    if (getDestinationCountry.data != null) {
                        for (i in 0 until getDestinationCountry.data.recordsInfo.size) {
                            arrayUtmSourceList.add(getDestinationCountry.data.recordsInfo[i])
                        }
                    } else {

                        CommonUtils.toast(requireActivity, "Failed to retrieve destinationCountry. Please try again.")
                    }

                    setUtmSourceDropDownList(utmSource)

                } else {
                    CommonUtils.toast(
                        requireActivity, getDestinationCountry.message ?: " Failed"
                    )
                }
            }
        }
    }
    private fun setUtmSourceDropDownList(utmSource: TextView) {
        utmSource.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Country")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = utmSource.width
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                utmSource.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + utmSource.height
            } else {
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + utmSource.height
            }

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterUtmSourceList(requireActivity(), arrayUtmSourceList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                utmSource.text = selectedCountry.label
                sourceCountry = selectedCountry.value
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(utmSource)
            layout.requireViewById<EditText>(R.id.etSelect)
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
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getLeadDestinationCountry(requireActivity: FragmentActivity, destinationCountry: TextView) {
        ViewModalClass().getlead_destination_countryData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: GetStudentResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    if (getDestinationCountry.data != null) {
                        for (i in 0 until getDestinationCountry.data.size) {
                            arrayLeadDestinationCountryList.add(getDestinationCountry.data[i])
                        }


                    } else {

                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setLeadDestinationList(destinationCountry)

                } else {
                    CommonUtils.toast(
                        requireActivity, getDestinationCountry.message ?: " Failed"
                    )
                }
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun setLeadDestinationList(destinationCountry: TextView) {
        destinationCountry.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Country")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = destinationCountry.width
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                destinationCountry.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + destinationCountry.height
            } else {
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + destinationCountry.height
            }

            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterCountryList(requireActivity(), arrayLeadDestinationCountryList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                destinationCountry.text = selectedCountry.label
                sourceCountry = selectedCountry.value
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(destinationCountry)
            layout.requireViewById<EditText>(R.id.etSelect)
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
    private fun getSourceStateList(requireActivity: FragmentActivity, sourceState: TextView) {

        if (sourceCountry.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Select country first")
            return
        }
        arraySourceStateList.clear()

        ViewModalClass().getLeadCountryStateListMutable(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, sourceCountry
        ).observe(requireActivity) { destination: GetStudentResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    if (getDestinationCountry.data != null) {
                        for (i in 0 until getDestinationCountry.data.size) {
                            arraySourceStateList.add(getDestinationCountry.data[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )

                    }

                    setDropDownSourceStateList(sourceState)

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDestinationCountry.message ?: " Failed"
                    )
                }
            }
        }


    }
    private fun setDropDownSourceStateList(sourceState: TextView) {
        sourceState.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search States")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = sourceState.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                sourceState.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + sourceState.height
            } else {
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + sourceState.height
            }

            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterCountryList(requireActivity(), arraySourceStateList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                sourceState.text = selectedCountry.label
                sourceCountry = selectedCountry.value

                // valueIdentifier = selectedCampus.value
                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }
            // Show the popup window
            popupWindow.showAsDropDown(sourceState)


            //search edit text
            layout.requireViewById<EditText>(R.id.etSelect)
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
    private fun getSourceCountryList(requireActivity: FragmentActivity, sourceCountry: TextView) {
        ViewModalClass().getLeadCountryListMutable(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: GetStudentResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {

                    if (getDestinationCountry.data != null) {
                        for (i in 0 until getDestinationCountry.data!!.size) {
                            arraySourceCountryList.add(getDestinationCountry.data!![i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        );
                    }

                    setDropDownSourceCountryList(sourceCountry)

                } else {
                    CommonUtils.toast(
                        requireActivity, getDestinationCountry.message ?: " Failed"
                    )
                }
            }
        }

    }
    private fun setDropDownSourceCountryList(studentList: TextView) {
        studentList.setOnClickListener {

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Country")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = studentList.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                studentList.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + studentList.height
            } else {
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + studentList.height
            }

            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterCountryList(requireActivity(), arraySourceCountryList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                studentList.text = selectedCountry.label
                sourceCountry = selectedCountry.value

                // valueIdentifier = selectedCampus.value
                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }
            // Show the popup window
            popupWindow.showAsDropDown(studentList)


            //search edit text
            layout.requireViewById<EditText>(R.id.etSelect)
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
    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false
    }
}