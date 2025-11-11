package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.adaptor.AdapterLeadForm
import com.student.Compass_Abroad.databinding.FragmentLeadFormBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.leadForm.LeadField


class LeadFormFragment : BaseFragment() {

    private lateinit var binding: FragmentLeadFormBinding
    private var options = ArrayList<String>()
    private var layoutManager:LinearLayoutManager?=null
    private var adapterLeadForm:AdapterLeadForm?=null
    private lateinit var arrayList1:List<LeadField>
    private var recyclerViewState: Parcelable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLeadFormBinding.inflate(inflater, container, false)

       setRecycler()


        return binding.root
    }




    private fun setRecycler() {
        // Initialize RecyclerView and its adapter
        layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFpAp.layoutManager = layoutManager
    }

    private fun displayFormDetails(formName: String, formDescription: String) {
         binding.formNameTextView.text = formName
         binding.formDescriptionTextView.text = formDescription
     }

    override fun onPause() {
        super.onPause()
        App.sharedPre?.clearKey("country")
    }

    /*  private fun createForm(leadFields: List<LeadField>) {
         val linearLayout = binding.formFieldsContainer
         val spinnerObjectMap =
             mutableMapOf<String, Spinner>() // Initialize an empty HashMap with String keys and Spinner values

         val list = mutableListOf<String>() // Initialize an empty list

         for (i in 0 until leadFields.size) {
             // Assuming options[i].value is a String
             val value = leadFields.get(i).name
             list.add(value) // Add the value to the list
             // If you want to display or use the value, you can do so here
             // For example: Toast.makeText(activity, "Selected Companies: $value", Toast.LENGTH_LONG).show()
         }
         Log.e("snnns", list.toString())

         leadFields.forEach { field ->
             val layoutParams = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT
             )

             val label = TextView(requireContext())
             label.text = field.label
             linearLayout.addView(label)


             when (field.type) {
                 "text", "email", "number" -> {
                     val editText = EditText(requireContext())
                     editText.hint = field.lead_form_field.placeholder
                     editText.layoutParams = layoutParams
                     editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                     editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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
                     val editText = EditText(requireContext())
                     editText.hint = field.lead_form_field.placeholder
                     editText.layoutParams = layoutParams
                     editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                     editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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
                         field,
                         leadFields,
                         list,
                         spinnerObjectMap
                     )

                 }

                 "multiple_select" -> {
                     val spinner = MultiSelectSpinner(requireActivity())
                     spinner.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val layoutParams = LinearLayout.LayoutParams(
                         LinearLayout.LayoutParams.MATCH_PARENT,
                         LinearLayout.LayoutParams.WRAP_CONTENT
                     )
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
                     spinner.setPadding(
                         horizontalPadding,
                         verticalPadding,
                         horizontalPadding,
                         verticalPadding
                     )
                     spinner.layoutParams = layoutParams


                     linearLayout.addView(spinner)


                     getAllMultiSelcetor(
                         requireActivity(),
                         field.options_data.url,
                         linearLayout,
                         spinner,
                         field.options_data,
                         field

                     )


                 }

                 "radio" -> {
                     val selectedDrawable: Drawable? = ContextCompat.getDrawable(
                         requireContext(),
                         R.drawable.ic_radio_button_checked
                     )
                     val unselectedDrawable: Drawable? = ContextCompat.getDrawable(
                         requireContext(),
                         R.drawable.ic_radio_button_unchecked
                     )

                     val radioGroup = RadioGroup(requireContext())
                     radioGroup.orientation = RadioGroup.VERTICAL
                     field.options_data?.options?.forEach { option ->
                         val radioButton = RadioButton(requireContext())
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
                     val editText = EditText(requireContext())
                     editText.hint = field.lead_form_field.placeholder
                     editText.layoutParams = layoutParams
                     editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                     editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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
                     val editText = EditText(requireContext())
                     editText.hint = field.lead_form_field.placeholder
                     editText.layoutParams = layoutParams
                     editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                     editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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
                     val editText = EditText(requireContext())
                     editText.hint = field.lead_form_field.placeholder
                     editText.layoutParams = layoutParams
                     editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                     editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                     val horizontalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
                     val verticalPadding =
                         resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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

             // Set layout weight based on column specification
             if (field.lead_form_field.column == "1/2") {
                 layoutParams.weight = 1f
             } else if (field.lead_form_field.column == "1/4") {
                 layoutParams.weight = 0.25f
             }
         }
     }

     private fun showDatePickerDialog(editText: EditText, type: String) {
         val calendar = Calendar.getInstance()
         val year = calendar.get(Calendar.YEAR)
         val month = calendar.get(Calendar.MONTH)
         val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

         clearEditTextFocus()

         val datePickerDialog = DatePickerDialog(
             requireActivity(), R.style.CustomDatePickerDialog,
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

     private fun showTimePickerDialog(editText: EditText, type: String) {
         val calendar = Calendar.getInstance()
         val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
         val minute = calendar.get(Calendar.MINUTE)

         clearEditTextFocus()

         val timePickerDialog = TimePickerDialog(
             requireContext(), R.style.CustomTimePickerDialog,
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

         val dialog = Dialog(requireContext())
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
         val currentFocus = requireActivity().currentFocus
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
         leadFields: List<LeadField>,
         list: MutableList<String>,
         spinnerObjectMap: MutableMap<String, Spinner>,
     ) {
         if (type == "manual" && options != null) {
             // If options data type is manual, create spinner with manual options
             createManualDropdown(options.options, linearLayout)

         } else if (type == "api") {
             // If options data type is API, fetch options data from API and create spinner
             val countryApiUrl = options?.url ?: ""
             val spinner = Spinner(requireContext())
             linearLayout.addView(spinner)
             spinnerObjectMap[leadField.name] = spinner;
             val emptyStringArray: List<Data> = emptyList()
             createDropdownFromApi(
                 emptyStringArray,
                 linearLayout,
                 spinner,
                 options,
                 leadField,
                 leadFields,
                 list,
                 spinnerObjectMap,
             )
             getAllField(
                 requireActivity(),
                 countryApiUrl,
                 linearLayout,
                 spinner,
                 options,
                 leadField,
                 leadFields,
                 list,
                 spinnerObjectMap
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

                     // Toast.makeText(requireActivity(),"$param",Toast.LENGTH_LONG).show()

                     Log.e("URL", options.url + paramOperator + param);

                     getAllStatesForCountry(
                         requireActivity(),
                         options.url + paramOperator + param,
                         options.dependent_field_name,
                         linearLayout,
                         spinner,
                         options,
                         leadField,
                         leadFields,
                         list,
                         spinnerObjectMap
                     )

                      if (options.is_already_param_exist) {

                     }
                     else{
                         var param="";
                         var paramOp="?";
                         for (i in 0 until options.param_info.size) {
                             if(i!=0){
                                 paramOp="&";
                             }
                             param += paramOp + options.param_info.get(i).param_name + "=" + App.sharedPre?.getString(
                                 options.param_info.get(i).field_name,
                                 ""
                             )
                         }
                         getAllStatesForCountry(
                             requireActivity(),
                             options.url + param,
                             options.dependent_field_name,
                             linearLayout,
                             spinner,
                             options,
                             leadField
                         )
                     }


                } else {
                    val relatedFields = options.related_fields;

                    Log.e("related_field_1", relatedFields.toString());
                    for (i in 0 until relatedFields.size) {
                        val relatedSpinner = spinnerObjectMap.get(relatedFields.get(i));

                        if (relatedSpinner != null) {
                            val indexOfRelatedField = list.indexOf(relatedFields[i]);

                            val relatedLeadField = leadFields.get(indexOfRelatedField);

                            val emptyStringArrayRelatedField: List<Data> = emptyList()
                            createDropdownFromApi(
                                emptyStringArrayRelatedField,
                                linearLayout,
                                relatedSpinner,
                                relatedLeadField.options_data,
                                relatedLeadField,
                                leadFields,
                                list,
                                spinnerObjectMap,
                            )

                            Log.e("related_field_2", relatedFields.get(i));
                            Log.e("related_field_3", relatedLeadField.name);
                        }
                    }

                    Log.e("testing-dependent", options.related_fields.toString());
                    // For non-dependent fields, fetch options and enable spinner after data is loaded

                }
                false
            }


        }
    }


    private fun createManualDropdown(options: List<Option>, linearLayout: LinearLayout) {
        val spinner = Spinner(requireContext())
        val adapter = ArrayAdapter(
            requireContext(),
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
            resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
        val verticalPadding =
            resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
        spinner.setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        spinner.layoutParams = layoutParams
        binding.formFieldsContainer.addView(spinner)

    }

    private fun getAllField(
        requireActivity: FragmentActivity,
        url: String,
        linearLayout: LinearLayout,
        spinner: Spinner,
        options1: OptionsData?,
        leadField: LeadField,
        leadFields: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,
    ) {

        ViewModalClass().getAllFieldFormModalLiveData(
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
                        leadFields,
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

    private fun createDropdownFromApi(
        options: List<Data>,
        linearLayout: LinearLayout,
        spinner: Spinner,
        options1: OptionsData?,
        leadField: LeadField,
        leadFields: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,

        ) {
        // Handling null or empty placeholder
        var placeholderItem = leadField.lead_form_field.placeholder ?: "Select an option"

        // Combine placeholder item with the rest of the options
        val allOptions = listOf(placeholderItem) + options.map { it.label }
        val allValues = listOf("") + options.map { it.value }
        // Create an ArrayAdapter for the spinner
        val adapter = ArrayAdapter(
            requireContext(),
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
            resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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

        if (options1!!.related_fields.size > 0) {
            val relatedFields = options1.related_fields;

            Log.e("related_field_1", relatedFields.toString());


            // Set item selected listener
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItemLabel = parent?.getItemAtPosition(position).toString()
                    val selectedItemIndex = allOptions.indexOf(selectedItemLabel)
                    if (selectedItemIndex != -1 && selectedItemLabel != placeholderItem) {
                        var selectedValue = allValues[selectedItemIndex]

                        //  Toast.makeText(requireActivity(), "$selectedValue", Toast.LENGTH_SHORT).show()

                        if (options1 != null && options1.related_fields != null) {
                            for (i in 0 until options1.related_fields.size) {
                                val key = options1.related_fields[i]
                                if (App.sharedPre != null && key != null) {
                                    App.sharedPre!!.saveString(key, "")

                                }
                            }
                        }




                        App.sharedPre!!.saveString(leadField.name, "$selectedValue")


                        // Assuming selectedValue is the value you want to use
                        // Save the selected value into SharedPreferences
                        // App.sharedPre?.saveString(AppConstants.countryId, selectedValue)

                        // If the field is dependent, fetch related data

                    } else {
                        // Handle selection of the placeholder item

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }


        }
    }


    private fun createCheckboxes(
        optionsData: OptionsData,
        linearLayout: LinearLayout,
        leadField: LeadFormField
    ) {
        // Drawable resources for checkbox states
        val selectedDrawable: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.checkbox_selected)
        val unselectedDrawable: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.checkbox_unselected)

        // Iterate through each option and create a checkbox
        optionsData.options?.forEach { option ->
            val checkBox = CheckBox(requireContext()).apply {
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
        leadFields: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,

        ) {
        ViewModalClass().getAllFieldFormModalLiveData(
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
                    createNestedDropdownFromApi(
                        states,
                        linearLayout,
                        spinner,
                        options1,
                        leadField,
                        leadFields,
                        list,
                        spinnerObjectMap
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

        ViewModalClass().getAllFieldFormModalLiveData(
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

    override fun selectedIndices(indices: List<Int>) {

    }

    override fun selectedStrings(strings: List<String>) {
        // Toast.makeText(requireActivity(),strings.toString(),Toast.LENGTH_SHORT).show()

    }

    private fun createNestedDropdownFromApi(
        options: List<Data>,
        linearLayout: LinearLayout,
        spinner: Spinner,
         options1: OptionsData?,
        leadField: LeadField,
        leadFields: List<LeadField>,
        list: MutableList<String>,
        spinnerObjectMap: MutableMap<String, Spinner>,
    ) {
        // Handling null or empty placeholder
        var placeholderItem = leadField.lead_form_field.placeholder ?: "Select an option"


        // Combine placeholder item with the rest of the options
        val allOptions = listOf(placeholderItem) + options.map { it.label }
        val allValues = listOf("") + options.map { it.value }
        // Create an ArrayAdapter for the spinner
        val adapter = ArrayAdapter(
            requireContext(),
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
            resources.getDimensionPixelSize(R.dimen.edit_text_horizontal_padding)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.edit_text_vertical_padding)
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



        Log.e("testing-dependent", options1!!.related_fields.toString());
        // For no


    // Set item selected listener
    // Set item selected listener
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val selectedItemLabel = parent?.getItemAtPosition(position).toString()
            val selectedItemIndex = allOptions.indexOf(selectedItemLabel)
            if (selectedItemIndex != -1 && selectedItemLabel != placeholderItem) {
                var selectedValue = allValues[selectedItemIndex]

                //  Toast.makeText(requireActivity(), "$selectedValue", Toast.LENGTH_SHORT).show()

                if (options1 != null && options1.related_fields != null) {
                    for (i in 0 until options1.related_fields.size) {
                        val key = options1.related_fields[i]
                        if (App.sharedPre != null && key != null) {
                            App.sharedPre!!.saveString(key, "")

                        }
                    }
                }




                App.sharedPre!!.saveString(leadField.name, "$selectedValue")


                // Assuming selectedValue is the value you want to use
                // Save the selected value into SharedPreferences
                // App.sharedPre?.saveString(AppConstants.countryId, selectedValue)

                // If the field is dependent, fetch related data

            } else {
                // Handle selection of the placeholder item
                options1?.related_fields?.forEach { relatedField ->
                    val relatedSpinner = spinnerObjectMap[relatedField]
                    relatedSpinner?.let {
                        val indexOfRelatedField = list.indexOf(relatedField)
                        if (indexOfRelatedField != -1) {
                            val relatedLeadField = leadFields[indexOfRelatedField]
                            val emptyStringArrayRelatedField: List<Data> = emptyList()
                            createDropdownFromApi(
                                emptyStringArrayRelatedField,
                                linearLayout,
                                it,
                                relatedLeadField.options_data,
                                relatedLeadField,
                                leadFields,
                                list,
                                spinnerObjectMap
                            )
                        }
                    }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Do nothing
        }
    }
}
*/



}