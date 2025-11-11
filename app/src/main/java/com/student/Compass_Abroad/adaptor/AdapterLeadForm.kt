package com.student.Compass_Abroad.adaptor

import MultiSelectSpinner
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemLeadFormBinding

import com.student.Compass_Abroad.modal.allFieldResponse.Data
import com.student.Compass_Abroad.modal.allFieldResponse.formAllFieldResponse
import com.student.Compass_Abroad.modal.leadForm.LeadField
import com.student.Compass_Abroad.modal.leadForm.LeadFormField
import com.student.Compass_Abroad.modal.leadForm.Option
import com.student.Compass_Abroad.modal.leadForm.OptionsData

import com.student.Compass_Abroad.retrofit.ViewModalClass

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AdapterLeadForm(var requireActivity: FragmentActivity, var arrayList1: List<LeadField>) :RecyclerView.Adapter<AdapterLeadForm.MyViewHolder>(), MultiSelectSpinner.OnMultipleItemsSelectedListener {

  var adapter:ArrayAdapter<String>?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterLeadForm.MyViewHolder {
        val binding =
            ItemLeadFormBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterLeadForm.MyViewHolder(binding)
    }

    private fun showTimePickerDialog(editText: EditText, type: String) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        clearEditTextFocus()

        val timePickerDialog = TimePickerDialog(
            requireActivity, R.style.CustomTimePickerDialog,
            { _, selectedHourOfDay, selectedMinute ->
                val formatter = DecimalFormat("00")
                val timeText =
                    "${formatter.format(selectedHourOfDay)}:${formatter.format(selectedMinute)}"
                editText.setText(timeText)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            },
            hourOfDay, minute, true
        )

        when (type) {
            "past" -> {
                // Restrict time selection to current time if past type is selected
                val currentTime = System.currentTimeMillis()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                timePickerDialog.updateTime(currentHour, currentMinute)
            }

            "future" -> {
                // No restrictions on time selection for future type
            }

            "present" -> {
                // No restrictions on time selection for present type
            }

            else -> {
                // Default to no restrictions
            }
        }

        timePickerDialog.show()
    }

    private fun showDateTimePickerDialog(editText: EditText, type: String) {
        val calendar = Calendar.getInstance()

        val dialog = Dialog(requireActivity)
        dialog.setContentView(R.layout.dialog_date_time_picker)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)

        val confirmButton = dialog.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            if (type == "future") {
                adjustTimePickerForFuture(timePicker, calendar)
            }
        }

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
        }

        confirmButton.setOnClickListener {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault())
            val dateTime = formatter.format(calendar.time)
            editText.setText(dateTime)
            editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        when (type) {
            "past" -> {
                // Restrict date selection to dates before today's date
                datePicker.maxDate = System.currentTimeMillis()
            }

            "future" -> {
                // Restrict date selection to dates starting from today's date
                datePicker.minDate = System.currentTimeMillis()
                adjustTimePickerForFuture(timePicker, calendar)
            }

            "present" -> {
                // No restrictions on date selection
            }

            else -> {
                // Default to no restrictions
            }
        }

        dialog.show()
    }

    private fun adjustTimePickerForFuture(timePicker: TimePicker, calendar: Calendar) {
        val currentCalendar = Calendar.getInstance()
        val isCurrentDate = calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)

        if (isCurrentDate) {
            val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentCalendar.get(Calendar.MINUTE)

            timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                if (hourOfDay < currentHour) {
                    view.currentHour = currentHour
                    view.currentMinute = currentMinute
                } else if (hourOfDay == currentHour && minute < currentMinute) {
                    view.currentMinute = currentMinute
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                }
            }

            timePicker.currentHour = currentHour
            timePicker.currentMinute = currentMinute
        } else {
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
            }
        }
    }

    private fun clearEditTextFocus() {
        val currentFocus = requireActivity.currentFocus
        if (currentFocus is EditText) {
            currentFocus.clearFocus()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun createDropdown(
        type: String?,
        options: OptionsData?,
        linearLayout: LinearLayout,
        leadField: LeadField,
        holder: MyViewHolder,
        position: Int,
        arrayList1: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,

        ) {
        if (type == "manual" && options != null) {
            // If options data type is manual, create spinner with manual options
            createManualDropdown(options.options,holder)

        } else if (type == "api") {
            // If options data type is API, fetch options data from API and create spinner
            val countryApiUrl = options?.url ?: ""
            val spinner = Spinner(requireActivity)
            spinnerObjectMap[leadField.name] = spinner
            linearLayout.addView(spinner)

            val emptyStringArray: List<Data> = emptyList()
            createDropdownFromApi(
                emptyStringArray,
                linearLayout,
                spinner,
                options,
                leadField, position,arrayList1,list,spinnerObjectMap
            )
            getAllField(
                requireActivity,
                countryApiUrl,
                linearLayout,
                spinner,
                options,
                leadField, position,arrayList1,list,spinnerObjectMap

            )


            spinner.setOnTouchListener { view, motionEvent ->
                if (options!!.is_dependent) {

                    val paramOperator = if (options.is_already_param_exist) "" else "?"

                    var param: String? = "";
                    for (i in 0 until options.param_info.size) {
                        var joinOperator = "";

                        if (!options.is_already_param_exist && i != 0) {
                            joinOperator = "&"
                        }

                        if (options.is_already_param_exist) {
                            joinOperator = "&";
                        }

                        param += joinOperator + options.param_info.get(i).param_name + "=" + App.sharedPre?.getString(
                            options.param_info.get(i).field_name,
                            ""
                        )
                    }

                    Log.e("URL_PARAM", param.toString());

                    // Toast.makeText(requireActivity,"$param",Toast.LENGTH_LONG).show()

                    Log.e("URL", options.url + paramOperator + param);

                    getAllStatesForCountry(
                        requireActivity,
                        options.url + paramOperator + param,
                        options.dependent_field_name,
                        linearLayout,
                        spinner,
                        options,
                        leadField,position,arrayList1,list,spinnerObjectMap

                    )


                } else {
                    /*val relatedFields = options.related_fields;
                    Toast.makeText(requireActivity,"tjjj",Toast.LENGTH_SHORT).show()

                    for (i in 0 until  relatedFields.size) {
                        val relatedSpinner = spinnerObjectMap[relatedFields[i]]

                        if (relatedSpinner != null) {
                            val indexOfRelatedField = list.indexOf(relatedFields[i])
                            Toast.makeText(requireActivity, "inner", Toast.LENGTH_SHORT).show()
                            val relatedLeadField = arrayList1[indexOfRelatedField]
                            spinner.adapter=null
                            val emptyStringArrayRelatedField: List<Data> = emptyList()

                            createDropdownFromApi(
                                emptyStringArrayRelatedField,
                                linearLayout,
                                relatedSpinner,
                                relatedLeadField.options_data,
                                relatedLeadField, position, arrayList1, list, spinnerObjectMap
                            )
                            Log.e("related_field_2", relatedFields[i])
                            Log.e("related_field_3", relatedLeadField.name)
                        } else {
                            Log.e("related_field_error", "No spinner found for ${relatedFields[i]}")
                        }
                    }*/

                    Log.e("testing-dependent", options.related_fields.toString())
                }

            false
        }
    }



}


    private fun createManualDropdown(
        options: List<Option>,
        holder: MyViewHolder,
    ) {
        val spinner = Spinner(requireActivity)
        val adapter = ArrayAdapter(
            requireActivity,
            android.R.layout.simple_spinner_item,
            options.map { it.label }
        )
        spinner.adapter = adapter
        spinner.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val horizontalPadding =
            requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
        val verticalPadding =
            requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
        spinner.setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        spinner.layoutParams = layoutParams
        holder.binding.formFieldsContainer.addView(spinner)

    }

    private fun getAllField(
        requireActivity: FragmentActivity,
        url: String,
        linearLayout: LinearLayout,
        spinner: Spinner,
        options1: OptionsData?,
        leadField: LeadField,
        position: Int,
        arrayList1: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,
    ) {

        ViewModalClass().getCountryList(
            requireActivity,
            url,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { formResponseModal: formAllFieldResponse? ->
            formResponseModal?.let { nonNullCountryResponseModal ->
                if (formResponseModal.statusCode == 200) {
                    // If the API call is successful, populate the dropdown with the retrieved data
                    val options = nonNullCountryResponseModal.data ?: emptyList()
                    // Toast.makeText(requireActivity(),options1!!.is_dependent.toString(),Toast.LENGTH_SHORT).show()
                    createDropdownFromApi(
                        options,
                        linearLayout,
                        spinner,
                        options1,
                        leadField,
                        position,
                        this.arrayList1,
                        list,
                        spinnerObjectMap

                    )


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        formResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun createDropdownFromApi(
        options: List<Data>,
        linearLayout: LinearLayout,
        spinner: Spinner,
        options1: OptionsData?,
        leadField: LeadField,
        pos: Int,
        arrayList1: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,

        ) {
        // Handling null or empty placeholder
        var placeholderItem = leadField.lead_form_field.placeholder ?: "Select an option"
        // Combine placeholder item with the rest of the options

     val allOptions = listOf(placeholderItem) + options.map { it.label }
        val allValues = listOf("") + options.map { it.value }
        // Create an ArrayAdapter for the spinner
         adapter = ArrayAdapter(
            requireActivity,
            android.R.layout.simple_spinner_item,
            allOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = adapter

        // Set custom background and layout parameters for the spinner
        spinner.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val horizontalPadding =
            requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
        val verticalPadding =
            requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
        spinner.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        spinner.layoutParams = layoutParams
        // Set the default value if specified
        val defaultValue = leadField.lead_form_field.default_value
            if (defaultValue != null && allValues.contains(defaultValue)) {
                val defaultPosition = allValues.indexOf(defaultValue)
                spinner.setSelection(defaultPosition)
            } else {
                spinner.setSelection(0) // Default to placeholder
            }



            // Set item selected listener
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val selectedItemLabel = parent?.getItemAtPosition(position).toString()
                    val selectedItemIndex = allOptions!!.indexOf(selectedItemLabel)
                    if (selectedItemIndex != -1 && selectedItemLabel != placeholderItem) {

                        var selectedValue = allValues[selectedItemIndex]
                        // Toast.makeText(requireActivity(), "$selectedValue", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireActivity,"$selectedValue",Toast.LENGTH_SHORT).show()




                        if (options1 != null && options1.related_fields != null) {
                            for (i in 0 until options1.related_fields.size) {
                                val key = options1.related_fields[i]
                                if (App.sharedPre != null && key != null) {
                                    App.sharedPre!!.saveString(key, "")

                                }

                            }

                        }

                        App.sharedPre!!.saveString(leadField.name, "$selectedValue")


                    } else {
                        // Handle selection of the placeholder item

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }



    }



    private fun createCheckboxes(
        optionsData: OptionsData,
        linearLayout: LinearLayout,
        leadField: LeadFormField,
    ) {
        // Drawable resources for checkbox states
        val selectedDrawable: Drawable? =
            ContextCompat.getDrawable(requireActivity, R.drawable.checkbox_selected)
        val unselectedDrawable: Drawable? =
            ContextCompat.getDrawable(requireActivity, R.drawable.checkbox_unselected)

        // Iterate through each option and create a checkbox
        optionsData.options?.forEach { option ->
            val checkBox = CheckBox(requireActivity).apply {
                text = option.label
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setPadding(8, 8, 8, 8)

                // Set initial checked state
                isChecked = option.is_selected

                // Set state drawable for checkbox
                val selectorDrawable = StateListDrawable().apply {
                    addState(intArrayOf(android.R.attr.state_checked), selectedDrawable)
                    addState(intArrayOf(-android.R.attr.state_checked), unselectedDrawable)
                }
                buttonDrawable = selectorDrawable
            }

            // Add the checkbox to the linear layout
            linearLayout.addView(checkBox)
        }
    }




    private fun getAllStatesForCountry(
        requireActivity: FragmentActivity,
        link: String,
        param: String,
        linearLayout: LinearLayout,
        spinner: Spinner,
        options1: OptionsData,
        leadField: LeadField,
        position: Int,
        arrayList1: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,

        ) {
        ViewModalClass().getCountryList(
            requireActivity,
            link,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { stateResponse: formAllFieldResponse? ->
            stateResponse?.let { nonNullStateResponse ->
                if (stateResponse.statusCode == 200) {

                    // If the API call is successful, populate the dropdown with the retrieved data
                    val states = nonNullStateResponse.data ?: emptyList()
                    createDropdownFromApi(
                        states,
                        linearLayout,
                        spinner,
                        options1,
                        leadField, position, this.arrayList1, list, spinnerObjectMap
                    )
                } else {
                    CommonUtils.toast(
                        requireActivity,
                        stateResponse.message ?: "Failed to retrieve states."
                    )
                }
            }
        }
    }


    private fun getAllMultiSelcetor(
        requireActivity: FragmentActivity,
        url: String,
        linearLayout: LinearLayout,
        layout: Spinner,
        options1: OptionsData?,
        leadField: LeadField,

        ) {

        ViewModalClass().getCountryList(
            requireActivity,
            url,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { formResponseModal: formAllFieldResponse? ->
            formResponseModal?.let { nonNullCountryResponseModal ->
                if (formResponseModal.statusCode == 200) {
                    // If the API call is successful, populate the dropdown with the retrieved data
                    val options = nonNullCountryResponseModal.data ?: emptyList()


                    val list = mutableListOf<String>() // Initialize an empty list

                    for (i in 0 until options.size) {
                        // Assuming options[i].value is a String
                        val value = options[i].value
                        list.add(value) // Add the value to the list
                        // If you want to display or use the value, you can do so here
                        // For example: Toast.makeText(activity, "Selected Companies: $value", Toast.LENGTH_LONG).show()
                    }

                    val multiSelectSpinner = layout as MultiSelectSpinner
                    multiSelectSpinner.setItems(list!!)
                    multiSelectSpinner.hasNoneOption(true)
                    multiSelectSpinner.setSelection(intArrayOf(0))
                    multiSelectSpinner.setListener(this)

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        formResponseModal.message ?: " Failed"
                    )
                }
            }
        }

    }





    override fun getItemCount(): Int {
        return arrayList1.size
    }



    class MyViewHolder(
// Use the generated ViewBinding class
        var binding: ItemLeadFormBinding,

        ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(leadField: LeadField) {
            //binding.name.text = leadField.name
            // binding.et.text = leadField.label
        }
    }



    override fun selectedIndices(indices: List<Int>) {

    }

    override fun selectedStrings(strings: List<String>) {

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val linearLayout = holder.binding.formFieldsContainer

        linearLayout.removeAllViews()

        val spinnerObjectMap =
            mutableMapOf<String, Spinner>() // Initialize an empty HashMap with String keys and Spinner values

        val list = mutableListOf<String>() // Initialize an empty list

        for (i in 0 until arrayList1.size) {
            // Assuming options[i].value is a String
            val value = arrayList1.get(i).name
            list.add(value) // Add the value to the list
            // If you want to display or use the value, you can do so here
            // For example: Toast.makeText(activity, "Selected Companies: $value", Toast.LENGTH_LONG).show()
        }



        var field = arrayList1[position]
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        val label = TextView(requireActivity)
        label.text = field.label
        linearLayout.addView(label)



        when (field.type) {
            "text", "email", "number" -> {
                val editText = EditText(requireActivity)
                editText.hint = field.lead_form_field.placeholder
                editText.layoutParams = layoutParams
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                editText.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                editText.inputType = when (field.type) {
                    "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    "number" -> InputType.TYPE_CLASS_NUMBER
                    else -> InputType.TYPE_CLASS_TEXT
                }
                linearLayout.addView(editText)
            }

            "textarea" -> {
                val editText = EditText(requireActivity)
                editText.hint = field.lead_form_field.placeholder
                editText.layoutParams = layoutParams
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                editText.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                editText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                editText.minLines = 3
                editText.maxLines = 5
                linearLayout.addView(editText)
            }

            "checkbox" -> {
                createCheckboxes(field.options_data, linearLayout, field.lead_form_field)
            }

            "single_select" -> {
                createDropdown(
                    field.options_data?.type,
                    field.options_data,
                    linearLayout,
                    field,holder,position,arrayList1,list,spinnerObjectMap

                )

            }

            "multiple_select" -> {
                val spinner = MultiSelectSpinner(requireActivity)
                spinner.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                spinner.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                spinner.layoutParams = layoutParams


                linearLayout.addView(spinner)


                getAllMultiSelcetor(
                    requireActivity,
                    field.options_data.url,
                    linearLayout,
                    spinner,
                    field.options_data,
                    field

                )


            }

            "radio" -> {
                val selectedDrawable: Drawable? = ContextCompat.getDrawable(
                    requireActivity,
                    R.drawable.ic_radio_button_checked
                )
                val unselectedDrawable: Drawable? = ContextCompat.getDrawable(
                    requireActivity,
                    R.drawable.ic_radio_button_unchecked
                )

                val radioGroup = RadioGroup(requireActivity)
                radioGroup.orientation = RadioGroup.VERTICAL
                field.options_data?.options?.forEach { option ->
                    val radioButton = RadioButton(requireActivity)
                    radioButton.text = option.label
                    val selectorDrawable = StateListDrawable().apply {
                        addState(intArrayOf(android.R.attr.state_checked), selectedDrawable)
                        addState(intArrayOf(-android.R.attr.state_checked), unselectedDrawable)
                    }
                    radioButton.buttonDrawable = selectorDrawable
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    radioButton.setPadding(8, 8, 8, 8)
                    radioGroup.addView(radioButton)
                }
                linearLayout.addView(radioGroup)
            }

            "date" -> {
                val editText = EditText(requireActivity)
                editText.hint = field.lead_form_field.placeholder
                editText.layoutParams = layoutParams
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                editText.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                editText.inputType = InputType.TYPE_NULL
                editText.setOnClickListener {
                    showDatePickerDialog(editText, field.options_data?.type ?: "")
                }
                linearLayout.addView(editText)
            }

            "time" -> {
                val editText = EditText(requireActivity)
                editText.hint = field.lead_form_field.placeholder
                editText.layoutParams = layoutParams
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                editText.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                editText.inputType = InputType.TYPE_NULL
                editText.setOnClickListener {
                    showTimePickerDialog(editText, field.options_data?.type ?: "")
                }
                linearLayout.addView(editText)
            }

            "datetime" -> {
                val editText = EditText(requireActivity)
                editText.hint = field.lead_form_field.placeholder
                editText.layoutParams = layoutParams
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                val horizontalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                val verticalPadding =
                    requireActivity.resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                editText.setPadding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
                )
                editText.inputType = InputType.TYPE_NULL
                editText.setOnClickListener {
                    showDateTimePickerDialog(editText, field.options_data?.type ?: "")
                }
                linearLayout.addView(editText)
            }


            // Handle other types as needed
        }
        if (field.lead_form_field.column == "1/2") {
            layoutParams.weight = 1f
        } else if (field.lead_form_field.column == "1/4") {
            layoutParams.weight = 0.25f
        }
    }

    private fun showDatePickerDialog(editText: EditText, type: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        clearEditTextFocus()

        val datePickerDialog = DatePickerDialog(
            requireActivity, R.style.CustomDatePickerDialog,
            { _, y, m, d ->
                val selectedYear = y
                val selectedMonth = m + 1
                val selectedDayOfMonth = d
                val formatter = DecimalFormat("00")
                val text =
                    "${formatter.format(selectedDayOfMonth)}/${formatter.format(selectedMonth)}/$selectedYear"
                editText.setText(text)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            }, year, month, dayOfMonth
        )

        when (type) {
            "past" -> {
                // Allow selecting dates only up to today's date
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            }

            "present" -> {
                // No restrictions on date selection
            }

            "future" -> {
                // Allow selecting dates starting from today's date
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            }

            else -> {
                // Default to no restrictions
            }
        }

        datePickerDialog.show()
    }


}


