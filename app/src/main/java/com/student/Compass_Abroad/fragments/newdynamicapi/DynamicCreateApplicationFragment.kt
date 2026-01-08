package com.student.Compass_Abroad.newdynamicapi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentDynamicCreateApplicationBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.createDynamicApplication.CreateDynamicApplication
import com.student.Compass_Abroad.modal.createDynamicApplication.FormField
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import java.util.Calendar
import java.util.Locale


data class UnifiedDropdownItem(
    val value: String,
    val label: String
)
class DynamicCreateApplicationFragment : BaseFragment() {
    private lateinit var binding: FragmentDynamicCreateApplicationBinding
    private lateinit var viewModel: ViewModalClass
    private val dependentSpinners: MutableMap<String, Spinner> = mutableMapOf()
    private val fieldValues: MutableMap<String, Any> = mutableMapOf()
    private val editTextFields: MutableMap<String, EditText> = mutableMapOf()
    private var formFields: List<FormField> = emptyList()
    private val fieldViews = mutableMapOf<String, View>()
    private val dropdownData = mutableMapOf<String, List<UnifiedDropdownItem>>()

    lateinit var form_identifier:String
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDynamicCreateApplicationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ViewModalClass::class.java]

        fetchLeadForm()

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


        setData()

        binding.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        return binding.root
    }

    private fun setData() {

        fieldValues["lead_id"] = sharedPre!!.getString(AppConstants.USER_ID, "").toString()
    }

    private fun fetchLeadForm() {
        viewModel.createDynamicApplication(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            "application"
        ).observe(requireActivity()) { leadFormResponse: CreateDynamicApplication? ->
            leadFormResponse?.let { response ->
                if (response.statusCode == 200 && response.success) {
                    response.data?.let { formData ->
                        formData.name ?: "Submit"
                        formFields = formData.fields?.get("1") ?: emptyList()
                        form_identifier=formData.identifier
                       if(isAdded){
                           generateDynamicForm(formFields)
                       }
                    }
                } else {
                    CommonUtils.toast(
                        requireActivity(),
                        response.message ?: "Failed to fetch form"
                    )
                }
            }
        }
    }

    private fun generateDynamicForm(fields: List<FormField>) {
        binding.formContainer.removeAllViews()

        // Sort fields by order_by
        val sortedFields = fields.sortedBy { it.order_by }

        sortedFields.forEach { field ->
            if (field.is_hidden == 0) { // Only show visible fields
                val fieldView = createFieldView(field)
                fieldView?.let {
                    binding.formContainer.addView(it)
                    fieldViews[field.name] = it

                    if (field.field_options != null) {
                        when (field.field_options.type) {
                            "api" -> {
                                if (isAdded) {
                                    loadDropdownData(field)
                                }
                            }
                            "manual" -> {
                                // Load manual options directly
                                loadManualOptions(field)
                            }
                        }
                    }
                }
            }
        }

        addLogButton()
    }

    private fun loadManualOptions(field: FormField) {
        val manualOptions = field.field_options?.options
        if (!manualOptions.isNullOrEmpty()) {
            val items = manualOptions.mapNotNull { option ->
                val value = option["value"]?.toString()
                val label = option["label"]?.toString()

                if (!value.isNullOrEmpty() && !label.isNullOrEmpty()) {
                    UnifiedDropdownItem(value, label)
                } else null
            }

            dropdownData[field.name] = items
            updateSpinnerAdapter(field.name, items, field.placeholder)

            Log.d("ManualOptions", "Loaded ${items.size} manual options for ${field.name}")
        } else {
            Log.w("ManualOptions", "No manual options found for ${field.name}")
        }
    }

    private fun createFieldView(field: FormField): View? {
        val context = requireContext()
        return when (field.type) {
            "single_select" -> createSingleSelectView(field, context)
            "multi_select" -> createMultiSelectView(field, context)
            "text" -> createTextView(field, context)
            "mobile" -> addDynamicPhoneInputWithCountryCode(field,context)
            "email" -> createEmailView(field, context)
            "date" -> createDateView(field, context)
            else -> null
        }
    }

    private fun createSingleSelectView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 32)
            }
        }

        val label = TextView(context).apply {
            text = getStyledLabel(field.label, field.validations?.required == true)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 8)
            }
        }

        val spinner = SearchableSpinner(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                CommonUtils.dpToPx(45, context)
            )
            tag = field.name
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
        }

        dependentSpinners[field.name] = spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedItem = dropdownData[field.name]?.get(position - 1)
                    selectedItem?.let {
                        val previousValue = fieldValues[field.name]

                        // Only update if value actually changed
                        if (previousValue != it.value) {
                            fieldValues[field.name] = it.value

                            // Clear and reload dependent children
                            clearAndReloadDependentChildren(field)
                        }
                    }
                } else {
                    val previousValue = fieldValues[field.name]

                    // Only clear if there was a value before
                    if (previousValue != null) {
                        fieldValues.remove(field.name)
                        clearAndReloadDependentChildren(field)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        container.addView(label)
        container.addView(spinner)

        return container
    }

    private fun clearAndReloadDependentChildren(field: FormField) {
        field.assigned_field_options?.children_fields?.forEach { childFieldName ->
            // Find the child field definition
            val childField = formFields.find { it.name == childFieldName } ?: return@forEach

            // Clear the stored value
            fieldValues.remove(childFieldName)

            // Clear spinner dropdowns (single select)
            dependentSpinners[childFieldName]?.let { spinner ->
                updateSpinnerAdapter(
                    childFieldName,
                    emptyList(),
                    childField.placeholder
                )
            }

            // Clear MultiSelect TextView
            val view = fieldViews[childFieldName]
            if (view is TextView && view.tag == childFieldName) {
                view.text = childField.placeholder
                view.setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
            }

            // Recursively clear deeper dependencies FIRST
            clearAndReloadDependentChildren(childField)

            // NOW reload the dropdown data for this child
            if (childField.field_options != null) {
                when (childField.field_options.type) {
                    "api" -> {
                        Log.d("ReloadDropdown", "Reloading dropdown for: $childFieldName")
                        loadDropdownData(childField)
                    }
                    "manual" -> {
                        loadManualOptions(childField)
                    }
                }
            }
        }
    }

    private fun createMultiSelectView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 32)
            }
        }

        val label = TextView(context).apply {
            text = getStyledLabel(field.label, field.validations?.required == true)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 8)
            }
        }

        val selectedTextView = TextView(context).apply {
            textSize = 14f
            setPadding(16, 16, 16, 16)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tag = field.name
            isClickable = true
            setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            isSingleLine = false
            maxLines = 10

            val currentSelected =
                (fieldValues[field.name] as? List<*>)?.map { it.toString() } ?: emptyList()
            val items = dropdownData[field.name]
            if (!items.isNullOrEmpty() && currentSelected.isNotEmpty()) {
                val selectedItems = items.filter { currentSelected.contains(it.value) }
                text = selectedItems.joinToString("\n") { it.label }
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
            } else {
                text = field.placeholder
            }

            setOnClickListener {
                val list = dropdownData[field.name]
                if (list.isNullOrEmpty()) {
                    CommonUtils.toast(context, "No options available.")
                    return@setOnClickListener
                }

                val selected =
                    (fieldValues[field.name] as? List<*>)?.map { it.toString() } ?: emptyList()
                val checkedItems = BooleanArray(list.size) { i -> selected.contains(list[i].value) }
                val selectedItems = list.filterIndexed { i, _ -> checkedItems[i] }.toMutableList()

                AlertDialog.Builder(context)
                    .setTitle(field.label)
                    .setMultiChoiceItems(
                        list.map { it.label }.toTypedArray(),
                        checkedItems
                    ) { _, which, isChecked ->
                        val item = list[which]

                        // Update selection
                        if (isChecked) selectedItems.add(item)
                        else selectedItems.removeAll { it.value == item.value }

                        // New values
                        val previousValues =
                            (fieldValues[field.name] as? List<*>)?.map { it.toString() }
                                ?: emptyList()
                        val newValues = selectedItems.map { it.value }

                        if (previousValues != newValues) {
                            fieldValues[field.name] = newValues

                            // Update text UI immediately
                            if (newValues.isEmpty()) {
                                text = field.placeholder
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.darker_gray
                                    )
                                )
                            } else {
                                text = selectedItems.joinToString("\n") { it.label }
                                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                            }

                            // ✅ Clear dependent child fields
                            clearDependentChildren(field)
                        }
                    }
                    .setPositiveButton("OK", null)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        container.addView(label)
        container.addView(selectedTextView)

        return container
    }

    private fun createTextView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, CommonUtils.dpToPx(16, context)) // Reduced bottom margin
            }
        }

        val label = TextView(context).apply {
            text = getStyledLabel(field.label, field.validations?.required == true)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, CommonUtils.dpToPx(4, context)) // Reduced space below label
            }
        }

        val editText = EditText(context).apply {
            hint = field.placeholder
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT // Use wrap_content to avoid excessive height
            )
            minHeight = CommonUtils.dpToPx(40, context) // Optional minimum height
            setPadding(CommonUtils.dpToPx(12, context), CommonUtils.dpToPx(8, context),
                CommonUtils.dpToPx(12, context), CommonUtils.dpToPx(8, context)) // Compact padding
            tag = field.name
            isEnabled = field.is_readonly == 0
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))

            // Store previous value for comparison
            var previousValue: String? = fieldValues[field.name] as? String

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s?.toString()?.trim() ?: ""
                    if (previousValue != newValue) {
                        previousValue = newValue
                        fieldValues[field.name] = newValue
                        clearDependentChildren(field)
                    }
                }
            })
        }

        editTextFields[field.name] = editText

        container.addView(label)
        container.addView(editText)

        return container
    }
    private fun addDynamicPhoneInputWithCountryCode(x1: FormField, context: Context): View {

        val labelText = x1.label.replaceFirstChar {

            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()

        }

        val spannableLabel = SpannableString("$labelText${if (x1.validations.required == true) " *" else ""}")
        if (x1.validations.required == true) {
            spannableLabel.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)), // Red color for the star
                labelText.length + 1,
                spannableLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val phoneLabel = TextView(context).apply {
            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        val inputHeight = context.resources.getDimensionPixelSize(R.dimen.dp_40)

        // Country Code Picker
        val ccp = com.hbb20.CountryCodePicker(context).apply {
            setDefaultCountryUsingNameCode("IN")
            setCountryForPhoneCode(91)
            showFlag(true)
            showFullName(false)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_country_code_no)
            setContentColor(ContextCompat.getColor(context, R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 50f, context.resources.displayMetrics
                ).toInt(), inputHeight
            )
        }

        // Phone Number EditText
        val phoneEditText = EditText(context).apply {
            hint = "Enter ${x1.label.lowercase()}"
            inputType = InputType.TYPE_CLASS_PHONE
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_phone_no)
            setPadding(16, 0, 16, 0)
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, inputHeight, 1f)
        }

        // Helper to update combined mobile value dynamically
        fun updateMobileValue() {
            val code = "+${ccp.selectedCountryCode}"
            val mobile = phoneEditText.text.toString().trim()
            fieldValues[x1.name] = "$code-$mobile" // ✅ Dynamic: uses x1.name
        }

        ccp.setOnCountryChangeListener { updateMobileValue() }

        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateMobileValue() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Horizontal container for CCP + EditText
        val phoneContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = context.resources.getDimensionPixelSize(R.dimen.dp_8)
            }
            addView(ccp)
            addView(phoneEditText)
        }

        // Vertical container: label + input
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, context.resources.getDimensionPixelSize(R.dimen.dp_8)) }
            addView(phoneLabel)
            addView(phoneContainer)
        }
    }

    private fun createEmailView(field: FormField, context: Context): View {
        val container = createTextView(field, context)
        val editText = container.findViewWithTag<EditText>(field.name)
        editText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        return container
    }

    private fun createDateView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 32)
            }
        }

        // Label
        val label = TextView(context).apply {
            text = field.label + if (field.validations?.required == true) " *" else ""
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        // Date picker EditText with background resource
        val dateEditText = EditText(context).apply {
            hint = field.placeholder
            isFocusable = false
            isClickable = true
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tag = field.name
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)

            setOnClickListener {
                showDatePicker { selectedDate ->
                    setText(selectedDate)
                    fieldValues[field.name] = selectedDate
                }
            }
        }

        container.addView(label)
        container.addView(dateEditText)

        return container
    }


    private fun fetchDropdownData(
        url: String,
        params: Map<String, String>,
        callback: (List<UnifiedDropdownItem>) -> Unit
    ) {
        val finalUrl = buildFinalUrl(url, params)
        Log.d("DropdownData", "Loading data with final URL: $finalUrl")

        viewModel.dropdownUrlFromApi(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre!!.getString(AppConstants.Device_IDENTIFIER, "").toString(),
            "Bearer ${CommonUtils.accessToken}",
            finalUrl
        ).observe(requireActivity()) { response ->
            val items = if (response?.success == true && response.statusCode == 200) {
                try {
                    when (val rawData = response.data) {
                        is List<*> -> rawData.mapNotNull { item ->
                            val mapItem = item as? Map<*, *>
                            val rawValue = mapItem?.get("value")
                            val label = mapItem?.get("label")?.toString()?.trim()
                            val value = when (rawValue) {
                                is Double -> rawValue.toInt().toString()
                                is Float -> rawValue.toInt().toString()
                                is Int -> rawValue.toString()
                                is Long -> rawValue.toString()
                                else -> rawValue?.toString()?.trim()?.removeSuffix(".0")
                            }
                            if (!value.isNullOrEmpty() && !label.isNullOrEmpty()) {
                                UnifiedDropdownItem(value, label)
                            } else null
                        }
                        is Map<*, *> -> {
                            val records = rawData["recordsInfo"] as? List<*>
                            records?.mapNotNull { item ->
                                val mapItem = item as? Map<*, *>
                                val rawValue = mapItem?.get("value")
                                val label = mapItem?.get("label")?.toString()?.trim()
                                val value = when (rawValue) {
                                    is Double -> rawValue.toInt().toString()
                                    is Float -> rawValue.toInt().toString()
                                    is Int -> rawValue.toString()
                                    is Long -> rawValue.toString()
                                    else -> rawValue?.toString()?.trim()?.removeSuffix(".0")
                                }
                                if (!value.isNullOrEmpty() && !label.isNullOrEmpty()) {
                                    UnifiedDropdownItem(value, label)
                                } else null
                            } ?: emptyList()
                        }
                        else -> {
                            Log.e("DropdownData", "Unexpected data format: $rawData")
                            emptyList()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DropdownParser", "Error parsing dropdown data: ${e.localizedMessage}", e)
                    emptyList()
                }
            } else {
                Log.e(
                    "DropdownData",
                    "API Error - success: ${response?.success}, statusCode: ${response?.statusCode}, message: ${response?.message}"
                )
                CommonUtils.toast(requireActivity(), response?.message ?: "Failed to load dropdown data")
                emptyList()
            }

            Log.d("DropdownData", "Final parsed items count: ${items.size}")
            callback(items)
        }
        }

    private fun buildFinalUrl(baseUrl: String, params: Map<String, String>): String {
        if (params.isEmpty()) return baseUrl

        val encodedParams = params.entries.joinToString("&") { (key, value) ->
            "${Uri.encode(key)}=${Uri.encode(value)}"
        }

        val finalUrl = if (baseUrl.contains("?")) {
            "$baseUrl&$encodedParams"
        } else {
            "$baseUrl?$encodedParams"
        }

        // Log the full final URL
        Log.d("FinalURLBuilder", "Base: $baseUrl")
        Log.d("FinalURLBuilder", "Params: $params")
        Log.d("FinalURLBuilder", "Final URL: $finalUrl")

        return finalUrl
    }

    private fun loadDropdownData(field: FormField) {
        val url = field.field_options?.url ?: return
        val params = mutableMapOf<String, String>()

        // Use assigned_field_options.params if defined, else fallback to field_options.params
        val paramList =
            field.assigned_field_options?.params ?: field.field_options?.params ?: emptyList()

        for (param in paramList) {
            val key = param.name
            val formKey = param.form_field_key ?: key
            val value = fieldValues[formKey]?.toString()

            if (param.required) {
                if (value.isNullOrEmpty()) {
                    Log.w("DropdownData", "Required param $key is missing for ${field.name}")
                    // Show placeholder if parent value not selected yet
                    updateSpinnerAdapter(field.name, emptyList(), field.placeholder)
                    return
                } else {
                    params[key] = value
                }
            } else {
                value?.let {
                    params[key] = it
                }
            }
        }

        Log.d("DropdownData", "Loading data for ${field.name} with url=$url, params=$params")

        fetchDropdownData(url, params) { data ->
            dropdownData[field.name] = data
            updateSpinnerAdapter(field.name, data, field.placeholder)
        }
    }

    private fun updateSpinnerAdapter(fieldName: String, data: List<UnifiedDropdownItem>, placeholder: String?) {
        val spinner = dependentSpinners[fieldName] ?: return

        val labels = mutableListOf<String>().apply {
            add(placeholder ?: "Select")
            addAll(data.map { it.label })
        }

        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, labels) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (position == 0) android.R.color.darker_gray else android.R.color.black
                    )
                )
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // preserve listener and set null while we are programmatically updating adapter/selection
        val savedListener = spinner.onItemSelectedListener
        try {
            spinner.onItemSelectedListener = null
            spinner.adapter = adapter

            // figure out selected index based on stored value
            val selectedValue = fieldValues[fieldName]?.toString()
            val selectedIndex = if (selectedValue != null) {
                data.indexOfFirst { it.value == selectedValue } + 1 // +1 for placeholder
            } else 0

            spinner.setSelection(if (selectedIndex > 0) selectedIndex else 0)
        } finally {
            // restore listener
            spinner.onItemSelectedListener = savedListener
        }
    }

    private fun handleDependentFields(parentField: FormField, selectedValue: String) {
        formFields.forEach { childField ->
            if (childField.assigned_field_options?.is_dependent == true &&
                childField.assigned_field_options.parent_field_name == parentField.name
            ) {

                fieldValues.remove(childField.name)
                dependentSpinners[childField.name]?.adapter = null
                updateSpinnerAdapter(childField.name, emptyList(), childField.placeholder)
                handleDependentFields(childField, "")  // clear downstream

                // Reload if necessary
                loadDropdownData(childField)
            }
        }
    }
    private fun clearDependentChildren(field: FormField) {
        field.assigned_field_options?.children_fields?.forEach { childFieldName ->
            // Clear value
            fieldValues.remove(childFieldName)

            // Clear spinner dropdowns
            dependentSpinners[childFieldName]?.let { spinner ->
                updateSpinnerAdapter(
                    childFieldName,
                    emptyList(),
                    formFields.find { it.name == childFieldName }?.placeholder ?: "Select"
                )
            }

            // Clear MultiSelect TextView (selectedTextView)
            val view = fieldViews[childFieldName]
            if (view is TextView && view.tag == childFieldName) {
                view.text = formFields.find { it.name == childFieldName }?.placeholder ?: "Select"
                view.setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
            }

            // Recursive clear for deeper dependencies
            val childField = formFields.find { it.name == childFieldName }
            childField?.let {
                clearDependentChildren(it)
            }
        }
    }
    private fun showDatePicker(callback: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                callback(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    private fun validateForm(): Boolean {
        val errorMessages = mutableListOf<String>()

        formFields.forEach { field ->
            if (field.validations?.required == true && field.is_hidden == 0) {
                val value = fieldValues[field.name]

                if (value == null ||
                    (value is String && value.trim().isEmpty()) ||
                    (value is List<*> && value.isEmpty())
                ) {

                    errorMessages.add("${field.label} is required.")
                }
            }
        }

        return if (errorMessages.isNotEmpty()) {
            val combinedMessage = errorMessages.joinToString("\n")
            errorDialogOpen(requireActivity(), combinedMessage)
            false
        } else {
            true
        }
    }

    private fun addLogButton() {
        val context = requireContext()
        val button = Button(context).apply {
            text = "Create Application"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                val marginInPx = CommonUtils.dpToPx(30, context)
                setMargins(marginInPx, 16, marginInPx, 16)
            }
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_btn_primary)
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }

        button.setOnClickListener {

            submitForm()
        }

        binding.formContainer.addView(button)
    }
    private fun submitForm() {
        if (validateForm()) {
            val formData = mutableMapOf<String, Any>()
            formFields.forEach { field ->
                fieldValues[field.name]?.let { value ->
                    val cleanValue = when (value) {
                        is List<*> -> value.mapNotNull { formatNumber(it) }
                        else -> formatNumber(value)
                    }
                    formData[field.name] = cleanValue
                }
            }

            val payload = SubmitPayload(
                form_identifier = form_identifier,
                data = formData
            )

            Log.d("CreateApplication", "Payload: $payload")

            viewModel.createApplication(
                requireActivity(),
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                payload
            ).observe(requireActivity()) { response ->
                if (response?.success == true) {
                    val bundle = Bundle().apply {
                        putString("status", "1")
                    }

                    App.singleton?.createApplicationIdentifier = response.data?.identifier?.toString()


                    response.data?.identifier?.toString()?.let { Log.d("ApplicationsubmitForm",it) }

                    binding.root.findNavController().navigate(R.id.uploadProgramDocFragment, bundle)
                    Toast.makeText(requireContext(), "Application submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed: ${response?.message ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun formatNumber(value: Any?): String {
        return when (value) {
            is Double -> if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
            is Float -> if (value % 1.0f == 0f) value.toInt().toString() else value.toString()
            is Int, is Long -> value.toString()
            else -> value?.toString() ?: ""
        }
    }
    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }
}