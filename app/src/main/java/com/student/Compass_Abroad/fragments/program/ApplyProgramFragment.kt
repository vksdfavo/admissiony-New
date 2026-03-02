package com.student.Compass_Abroad.fragments.program

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.databinding.FragmentApplyProgramBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.home.ProgramDetails
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.createDynamicApplication.CreateDynamicApplication
import com.student.Compass_Abroad.modal.createDynamicApplication.FormField
import com.student.Compass_Abroad.newdynamicapi.SubmitPayload
import com.student.Compass_Abroad.newdynamicapi.getStyledLabel
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.collections.set

import kotlin.text.equals
import kotlin.text.get
import kotlin.toString


// keep your UnifiedDropdownItem if used elsewhere
data class UnifiedDropdownItem(
    val value: String,
    val label: String
)

class ApplyProgramFragment : BaseFragment() {
    private lateinit var binding: FragmentApplyProgramBinding


    // state and caches (your original fields)
    private val dependentSpinners: MutableMap<String, Spinner> = mutableMapOf()
    private val fieldValues: MutableMap<String, Any> = mutableMapOf()
    private val editTextFields: MutableMap<String, EditText> = mutableMapOf()
    private var formFields: List<FormField> = emptyList()
    private val fieldViews = mutableMapOf<String, View>()
    private val dropdownData = mutableMapOf<String, List<UnifiedDropdownItem>>()
    lateinit var form_identifier: String

    // flicker prevention helpers
    private val spinnerInitGuard = mutableSetOf<String>() // fields currently being initialised -> ignore first selection event
    private val pendingRunnables = mutableMapOf<String, Runnable>() // for delayed dropdown checks
    private val handler = Handler(Looper.getMainLooper())

    // other fields you had
    private var prefer_course_id: String = ""
    private var intake_id: String = ""
    private val arrayListCampus = mutableListOf<com.student.Compass_Abroad.modal.GetCampusModal.Data>()
    private val arrayListCourses = mutableListOf<com.student.Compass_Abroad.modal.GetCampusModal.Data>()
    private val selectedCourses = mutableListOf<com.student.Compass_Abroad.modal.GetCampusModal.Data>()
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

    private var lead_identifier: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyProgramBinding.inflate(inflater, container, false)


       setdata()


        // fetch form after fieldValues seeded — we still fetch form, but pre-populated values exist
        fetchLeadForm()


        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
        lead_identifier = App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER, "")?.toString() ?: ""

        return binding.root
    }

    private fun setdata() {
        fieldValues["destination_country_id"] =
            details?.program?.institution?.country?.id.toString()
        fieldValues["institution_id"] =
            details?.program?.institution_id.toString()
        fieldValues["campus_id"] = details?.campus_id.toString()
        fieldValues["study_level_id"] =
            details?.program?.studylevel?.id.toString()
        fieldValues["preferred_program_id"] =details?.program?.id.toString()
        fieldValues["lead_id"] = App.sharedPre!!.getString(AppConstants.USER_ID, "").toString()
    }



    private fun fetchLeadForm() {
        ViewModalClass().createDynamicApplication(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            "application"
        ).observe(requireActivity()) { leadFormResponse: CreateDynamicApplication? ->
            leadFormResponse?.let { response ->
                if (response.statusCode == 200 && response.success) {
                    response.data?.let { formData ->
                        formFields = formData.fields?.get("1") ?: emptyList()
                        form_identifier = formData.identifier
                        if (isAdded) {
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
        // clear any pending runnables or guard state
        pendingRunnables.values.forEach { handler.removeCallbacks(it) }
        pendingRunnables.clear()
        spinnerInitGuard.clear()

        val sortedFields = fields.sortedBy { it.order_by }
        sortedFields.forEach { field ->
            if (field.is_hidden == 0) {
                val fieldView = createFieldView(field)
                fieldView?.let {
                    binding.formContainer.addView(it)
                    fieldViews[field.name] = it

                    // load dropdown if API-based
                    if (field.field_options != null && field.field_options.type == "api") {
                        // load dropdown data; safe to call even if parent not selected
                        loadDropdownData(field)
                    } else {
                        // for static options you might want to set preselected display if present (text fields)
                        // handled by createX methods themselves where relevant
                    }
                }
            }
        }
        addLogButton()
    }

    private fun createFieldView(field: FormField): View? {
        val context = requireContext()
        return when (field.type) {
            "single_select" -> createSingleSelectView(field, context)
            "multi_select" -> createMultiSelectView(field, context)
            "text" -> createTextView(field, context)
            "mobile" -> addDynamicPhoneInputWithCountryCode(field, context)
            "email" -> createEmailView(field, context)
            "date" -> createDateView(field, context)
            else -> null
        }
    }

    /**
     * SINGLE SELECT: Create spinner, but protect against onItemSelected firing when we set adapter/selection.
     * We use spinnerInitGuard to ignore the first selection event after programmatic setSelection.
     */
    private fun createSingleSelectView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        }

        val label = TextView(context).apply {
            text = getStyledLabel(field.label, field.validations?.required == true)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 8) }
        }

        val spinner = SearchableSpinner(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                CommonUtils.dpToPx(45, context)
            )
            tag = field.name
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
        }

        // make a safe listener that checks the init guard — if guard present, consume and ignore
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // If this field is in the initGuard, ignore this first programmatic event
                if (spinnerInitGuard.remove(field.name)) {
                    Log.d("FormDebug", "Ignored initial selection for ${field.name} (init guard).")
                    return
                }

                if (position > 0) {
                    val selectedItem = dropdownData[field.name]?.get(position - 1)
                    selectedItem?.let {
                        // store raw value string
                        fieldValues[field.name] = it.value

                        // reload dependents
                        handleDependentFields(field, it.value.toString())
                    }
                } else {
                    // placeholder
                    handleDependentFields(field, "")

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // set and store
        dependentSpinners[field.name] = spinner
        spinner.onItemSelectedListener = listener

        container.addView(label)
        container.addView(spinner)
        return container
    }

    /**
     * MULTI SELECT (EditText opens MultiChoice dialog).
     * Use a cancellable delayed check to update display after dropdown data arrives.
     */
    @SuppressLint("DefaultLocale")
    private fun createMultiSelectView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        }

        val labelText = field.label.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        val spannableLabel = SpannableString(
            "$labelText${if (field.validations?.required == true) " *" else ""}"
        )
        if (field.validations?.required == true) {
            spannableLabel.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
                labelText.length + 1,
                spannableLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val label = TextView(context).apply {
            text = spannableLabel
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 8) }
        }

        val editText = EditText(context).apply {
            tag = field.name
            hint = field.placeholder ?: field.label
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            val padding = context.resources.getDimensionPixelSize(R.dimen.dp_10)
            setPadding(padding, padding, padding, padding)
            isFocusable = false
            isClickable = true
            isSingleLine = false
            maxLines = 5
            tag = field.name
            if (field.is_readonly == 1) {
                isEnabled = false
                isClickable = false
                setBackgroundColor(ContextCompat.getColor(context, R.color.gradient_one))
            }
        }

        fun getSelectedValues(): List<String> {
            val stored = fieldValues[field.name]
            Log.d("FormDebug", "Stored value for ${field.name}: $stored (${stored?.javaClass?.name})")

            return when (stored) {
                is List<*> -> stored.map { it.toString() }
                is Array<*> -> stored.map { it.toString() }
                is Int -> listOf(stored.toString())
                is Double -> listOf(stored.toInt().toString())
                is String -> {
                    val cleaned = stored
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .trim()
                    when {
                        cleaned.contains(",") -> cleaned.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        cleaned.isNotEmpty() -> listOf(cleaned)
                        else -> emptyList()
                    }
                }
                else -> emptyList()
            }
        }

        fun updateEditTextWithSelectedValues() {
            val items = dropdownData[field.name]
            if (items.isNullOrEmpty()) {
                editText.setText("") // ensure blank until options available
                return
            }

            val valueToLabelMap = items.associate { it.value.toString() to it.label }
            val selectedValues = getSelectedValues().toMutableList()

            // convert single string "4" -> list ["4"]
            if (selectedValues.size == 1 && fieldValues[field.name] is String) {
                fieldValues[field.name] = selectedValues
                Log.d("FormDebug", "Auto-converted ${field.name} from single string to array: $selectedValues")
            }

            val selectedLabels = selectedValues.mapNotNull { valueToLabelMap[it] }
            if (selectedLabels.isNotEmpty()) {
                editText.setText(selectedLabels.joinToString(", "))
                editText.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                editText.setText("")
            }
        }

        // If dropdownData not ready, schedule cancellable retries (one per field)
        fun scheduleUpdateRetry() {
            pendingRunnables[field.name]?.let { handler.removeCallbacks(it) } // cancel previous
            val runnable = object : Runnable {
                override fun run() {
                    if (!dropdownData[field.name].isNullOrEmpty()) {
                        updateEditTextWithSelectedValues()
                        pendingRunnables.remove(field.name)
                    } else {
                        handler.postDelayed(this, 200)
                    }
                }
            }
            pendingRunnables[field.name] = runnable
            handler.postDelayed(runnable, 200)
        }

        // initial attempt
        updateEditTextWithSelectedValues()
        if (dropdownData[field.name].isNullOrEmpty()) {
            scheduleUpdateRetry()
        }

        editText.setOnClickListener {
            val list = dropdownData[field.name]
            if (list.isNullOrEmpty()) {
                CommonUtils.toast(context, "No options available.")
                return@setOnClickListener
            }

            val optionLabels = list.map { it.label }.toTypedArray()
            val optionValues = list.map { it.value.toString() }
            val selectedValues = getSelectedValues().toMutableList()

            val checkedItems = BooleanArray(optionValues.size) { i ->
                selectedValues.contains(optionValues[i])
            }

            AlertDialog.Builder(context)
                .setTitle("Select ${field.label}")
                .setMultiChoiceItems(optionLabels, checkedItems) { _, which, isChecked ->
                    val value = optionValues[which]
                    if (isChecked) {
                        if (!selectedValues.contains(value)) selectedValues.add(value)
                    } else {
                        selectedValues.remove(value)
                    }
                }
                .setPositiveButton("OK") { _, _ ->
                    // always store list
                    fieldValues[field.name] = selectedValues
                    updateEditTextWithSelectedValues()
                    clearDependentChildren(field)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        container.addView(label)
        container.addView(editText)
        editTextFields[field.name] = editText
        return container
    }

    private fun createTextView(field: FormField, context: Context): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, CommonUtils.dpToPx(16, context)) }
        }

        val label = TextView(context).apply {
            text = getStyledLabel(field.label, field.validations?.required == true)
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, CommonUtils.dpToPx(4, context)) }
        }

        val editText = EditText(context).apply {
            hint = field.placeholder
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            minHeight = CommonUtils.dpToPx(40, context)
            setPadding(
                CommonUtils.dpToPx(12, context),
                CommonUtils.dpToPx(8, context),
                CommonUtils.dpToPx(12, context),
                CommonUtils.dpToPx(8, context)
            )
            tag = field.name
            isEnabled = field.is_readonly == 0
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))

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
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
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

        val phoneEditText = EditText(context).apply {
            hint = "Enter ${x1.label.lowercase()}"
            inputType = InputType.TYPE_CLASS_PHONE
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_phone_no)
            setPadding(16, 0, 16, 0)
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, inputHeight, 1f)
        }

        fun updateMobileValue() {
            val code = "+${ccp.selectedCountryCode}"
            val mobile = phoneEditText.text.toString().trim()
            fieldValues[x1.name] = "$code-$mobile"
        }

        ccp.setOnCountryChangeListener { updateMobileValue() }
        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateMobileValue() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val phoneContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = context.resources.getDimensionPixelSize(R.dimen.dp_8) }
            addView(ccp)
            addView(phoneEditText)
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, context.resources.getDimensionPixelSize(R.dimen.dp_8)) }
            addView(phoneLabel)
            addView(phoneContainer)
        }
    }

    private fun createNumberView(field: FormField, context: Context): View {
        val container = createTextView(field, context)
        val editText = container.findViewWithTag<EditText>(field.name)
        editText?.inputType = InputType.TYPE_CLASS_NUMBER
        return container
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
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        }

        val label = TextView(context).apply {
            text = field.label + if (field.validations?.required == true) " *" else ""
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        val dateEditText = EditText(context).apply {
            hint = field.placeholder
            isFocusable = false
            isClickable = true
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
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

    /**
     * fetchDropdownData / buildFinalUrl — kept identical to your logic, but flood logs cleaned
     */
    private fun fetchDropdownData(
        url: String,
        params: Map<String, String>,
        callback: (List<UnifiedDropdownItem>) -> Unit
    ) {
        val finalUrl = buildFinalUrl(url, params)
        Log.d("DropdownData", "Loading data with final URL: $finalUrl")

        ViewModalClass().dropdownUrlFromApi(
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
        val finalUrl = if (baseUrl.contains("?")) "$baseUrl&$encodedParams" else "$baseUrl?$encodedParams"
        Log.d("FinalURLBuilder", "Final URL: $finalUrl")
        return finalUrl
    }

    /**
     * Load dropdown data — when data arrives we call updateSpinnerAdapter.
     * We intentionally do not trigger dependent reloads from adapter update; those are handled only when user actually changes selection.
     */
    private fun loadDropdownData(field: FormField) {
        val url = field.field_options?.url ?: return
        val params = mutableMapOf<String, String>()
        val paramList = field.assigned_field_options?.params ?: field.field_options?.params ?: emptyList()

        for (param in paramList) {
            val key = param.name
            val formKey = param.form_field_key ?: key
            val value = fieldValues[formKey]?.toString()

            if (param.required) {
                if (value.isNullOrEmpty()) {
                    Log.w("DropdownData", "Required param $key is missing for ${field.name}")
                    updateSpinnerAdapter(field.name, emptyList(), field.placeholder)
                    return
                } else {
                    params[key] = value
                }
            } else {
                value?.let { params[key] = it }
            }
        }

        Log.d("DropdownData", "Loading data for ${field.name} with url=$url, params=$params")

        // fetch and update
        lifecycleScope.launch {
            fetchDropdownData(url, params) { data ->
                dropdownData[field.name] = data
                updateSpinnerAdapter(field.name, data, field.placeholder)
            }
        }
    }

    /**
     * Update spinner adapter safely:
     * - set spinnerInitGuard so the first onItemSelected after programmatic setSelection is ignored
     * - temporarily remove listener while replacing adapter and setting selection (safe)
     */
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

            // add guard so the subsequent onItemSelected fired by setSelection is ignored
            if (selectedIndex > 0) {
                spinnerInitGuard.add(fieldName)
            }
            spinner.setSelection(if (selectedIndex > 0) selectedIndex else 0)
        } finally {
            // restore listener
            spinner.onItemSelectedListener = savedListener
        }
    }

    private fun handleDependentFields(parentField: FormField, selectedValue: String) {
        formFields.forEach { childField ->
            val assignedOptions = childField.assigned_field_options
            if (assignedOptions?.is_dependent == true &&
                assignedOptions.parent_field_name == parentField.name
            ) {
                // Reset child's UI & data
                updateSpinnerAdapter(childField.name, emptyList(), childField.placeholder)
                fieldValues.remove(childField.name)
                // clear deeper children

                if(childField.type.equals("multi_select")){
                    dropdownData.remove(childField.name)
                    val view = editTextFields[childField.name]
                    if (view is EditText && view.tag == childField.name) {
                        view.setText("")
                        view.setHint(formFields.find { it.name == childField.name }?.placeholder ?: "Select")
                        view.setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
                    }
                }

                handleDependentFields(childField, "")
                // Load child if parent selected now
                if (selectedValue.isNotEmpty()) {
                    loadDropdownData(childField)
                }


            }
        }
    }

    private fun clearDependentChildren(field: FormField) {
        field.assigned_field_options?.children_fields?.forEach { childFieldName ->
            // Remove stored value
            fieldValues.remove(childFieldName)

            // Reset spinner if child is single_select
            dependentSpinners[childFieldName]?.let { spinner ->
                updateSpinnerAdapter(
                    childFieldName,
                    emptyList(),
                    formFields.find { it.name == childFieldName }?.placeholder ?: "Select"
                )
            }

            // Reset multi_select EditText
            val view = fieldViews[childFieldName]
            if (view is EditText && view.tag == childFieldName) {
                view.setText("")
                view.setHint(formFields.find { it.name == childFieldName }?.placeholder ?: "Select")
                view.setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
            }

            // Reset text fields
            if (view is TextView && view.tag == childFieldName) {
                view.text = formFields.find { it.name == childFieldName }?.placeholder ?: "Select"
                view.setTextColor(ContextCompat.getColor(view.context, android.R.color.darker_gray))
            }

            // Recursively clear deeper dependencies
            val childField = formFields.find { it.name == childFieldName }
            childField?.let { clearDependentChildren(it) }
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
        } else true
    }

    private fun addLogButton() {
        val context = requireContext()
        val button = Button(context).apply {
            text = "Create Application"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                val marginInPx = CommonUtils.dpToPx(30, context)
                setMargins(marginInPx, 16, marginInPx, 16)
            }
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_btn_primary)
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }

        button.setOnClickListener { submitForm() }
        binding.formContainer.addView(button)
    }

    private fun submitForm() {
        if (!validateForm()) return

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

        val payload = SubmitPayload(form_identifier = form_identifier, data = formData)
        Log.d("CreateApplication", "Payload: $payload")

        ViewModalClass().createApplication(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            payload
        ).observe(requireActivity()) { response ->
            if (response?.success == true) {
                val bundle = Bundle().apply { putString("status", "1") }
                App.singleton?.createApplicationIdentifier=response.data?.identifier.toString()
                Log.e("jdjjdd",App.singleton?.createApplicationIdentifier.toString())
                binding.root.findNavController().navigate(R.id.applicationActiveFragment, bundle)
                // binding.root.findNavController().navigate(R.id.uploadProgramDocFragment, bundle)
                Toast.makeText(requireContext(), "Application submitted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed: ${response?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        // cleanup pending runnables
        pendingRunnables.values.forEach { handler.removeCallbacks(it) }
        pendingRunnables.clear()
        spinnerInitGuard.clear()
        super.onDestroyView()
    }
}

