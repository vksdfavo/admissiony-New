@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.student.Compass_Abroad.ApiResponseForm
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.DependentParam
import com.student.Compass_Abroad.FormField
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.databinding.FragmentSignUpBinding
import com.student.Compass_Abroad.fragments.PrivacyPolicyFragment
import com.student.Compass_Abroad.fragments.SubmitRequest
import com.student.Compass_Abroad.fragments.TermsAndConditionsFragment
import com.student.Compass_Abroad.newdynamicapi.UnifiedDropdownItem
import com.student.Compass_Abroad.retrofit.HomeViewModal
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import kotlin.random.Random

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: ViewModalClass
    private val dependentSpinners: MutableMap<String, Spinner> = mutableMapOf()
    private val fieldValues: MutableMap<String, String> = mutableMapOf()
    var contentKey = ""
    var statusValidation: Int? = null
    private val editTextFields: MutableMap<String, EditText> = mutableMapOf()
    val list = ArrayList<String>()
    var leadFormIdentifier:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[ViewModalClass::class.java]

        statusValidation = App.singleton!!.statusValidation

        binding.back.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        fetchLeadForm()

        return binding.root
    }

    private fun fetchLeadForm() {
        val currentFlavor = BuildConfig.FLAVOR.lowercase()
        Log.d("CurrentFlavor", "Detected flavor: $currentFlavor")

        // ✅ Decide identifier BEFORE API call
        val formIdentifier = when (currentFlavor) {
            "admisiony" -> "FRM1767768474477K26FORRU77"
            "zarnab" -> "FRM1764242607713U25JKBXD78"
            "edunetwork" -> "FRM1749619036014D25KMCAL44"
            else -> "FRM1749619036014D25KMCAL44"
        }

        HomeViewModal().leadFormResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer ${CommonUtils.accessToken}",
            formIdentifier
        ).observe(requireActivity()) { leadFormResponse: ApiResponseForm? ->

            leadFormResponse?.let { response ->

                if (response.statusCode == 200 && response.success) {

                    // ✅ Use forced identifier for specific flavors
                    leadFormIdentifier = when (currentFlavor) {
                        "mrconsultants" -> "FRM1766382941687O25HGYST84"
                        "zarnab" -> "FRM1764242607713U25JKBXD78"
                        "edunetwork" -> "FRM1749619036014D25KMCAL44"
                        else -> response.data?.identifier.orEmpty()
                    }

                    if (leadFormIdentifier.isNotEmpty()) {

                        fieldValues["form_identifier"] = leadFormIdentifier

                        val stepCount = response.data?.step_count ?: 0
                        val fieldsMap = response.data?.fields

                        for (step in 1..stepCount) {
                            val stepKey = step.toString()
                            val field = fieldsMap?.get(stepKey).orEmpty()

                            if (isAdded) {
                                createDynamicForm(field, "Submit")
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createDynamicForm(fields: List<FormField>, formName: String) {
        binding.formContainer.removeAllViews()
        fields.sortedBy { it.order_by }.forEach { field ->
            when (field.type) {
                "text", "email", "number" -> if (isAdded) {
                    createEditText(field)
                }

                "radio" -> if (isAdded) {

                    createRadioButton(field)
                }

                "single_select" -> if (isAdded) {
                    createSpinner(field, fields)
                }

                "mobile"->
                    if (isAdded) {
                        addDynamicPhoneInputWithCountryCode(field)
                    }

            }
        }

        val referralCode = arguments?.getString("referral") ?: ""

        val referralLabelText = if (referralCode.isNullOrEmpty()) {
            "Do you have any referral code?"
        } else {
            "Referral Code"
        }

        val spaceAfterFields = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_8)
            )
        }


        val spannableLabel = SpannableString(referralLabelText)
        val starStart = referralLabelText.indexOf("*")
        if (starStart != -1) {
            val starEnd = starStart + 1
            spannableLabel.setSpan(
                ForegroundColorSpan(Color.RED),
                starStart,
                starEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        val containerLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL  // or VERTICAL, based on your design
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.dp_12)
            }
        }

// 1. Create plain checkbox
        val termsCheckBox = CheckBox(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

// 2. Create clickable text separately

        val termsText = "I accept the Terms & Conditions and Privacy Policy."
        val spannableText = SpannableString(termsText)

        val termsStart = termsText.indexOf("Terms & Conditions")
        val termsEnd = termsStart + "Terms & Conditions".length
        val privacyStart = termsText.indexOf("Privacy Policy")
        val privacyEnd = privacyStart + "Privacy Policy".length

        spannableText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val fragmentManager = (widget.context as AppCompatActivity).supportFragmentManager
                if (fragmentManager.findFragmentByTag(TermsAndConditionsFragment::class.java.simpleName) == null) {
                    TermsAndConditionsFragment().show(fragmentManager, TermsAndConditionsFragment::class.java.simpleName)
                }
            }
        }, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val fragmentManager = (widget.context as AppCompatActivity).supportFragmentManager
                if (fragmentManager.findFragmentByTag(PrivacyPolicyFragment::class.java.simpleName) == null) {
                    PrivacyPolicyFragment().show(fragmentManager, PrivacyPolicyFragment::class.java.simpleName)
                }
            }
        }, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val termsTextView = TextView(requireContext()).apply {
            text = spannableText
            movementMethod = LinkMovementMethod.getInstance()
            setTextColor(Color.BLACK)
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                leftMargin = resources.getDimensionPixelSize(R.dimen.dp_8)
            }
        }

        containerLayout.addView(termsCheckBox)
        containerLayout.addView(termsTextView)

        binding.formContainer.addView(containerLayout)

        val loginText = "Already have an account? Login"

        val spannable = SpannableString(loginText)
        val loginStart = loginText.indexOf("Login")
        val loginEnd = loginStart + "Login".length
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            loginStart,
            loginEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(Color.BLACK),
            loginStart,
            loginEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.signInFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false  // Remove underline
            }
        }
        spannable.setSpan(clickableSpan, loginStart, loginEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val loginTextView = TextView(requireActivity()).apply {
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()  // Enable clickability
            gravity = Gravity.CENTER
            textSize = 14f
            setTextColor(Color.BLACK)  // Set overall text color
            setPadding(0, resources.getDimensionPixelSize(R.dimen.dp_10), 0, 0)
        }



        val submitButton = Button(requireActivity()).apply {
            text = formName
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_btn_dark)
            setTextColor(Color.WHITE)
            setOnClickListener {

                handleSubmit(termsCheckBox)
            }
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = resources.getDimensionPixelSize(R.dimen.dp_10)
        }
        submitButton.layoutParams = layoutParams

        binding.formContainer.addView(submitButton)
        binding.formContainer.addView(loginTextView)

    }

    private fun handleSubmit(termsCheckBox: CheckBox) {
        val formIdentifier = fieldValues["form_identifier"] as? String
        if (formIdentifier.isNullOrBlank()) {
            Toast.makeText(requireActivity(), "Form Identifier is missing.", Toast.LENGTH_LONG).show()
            return
        }

        val filteredEntries = fieldValues
            .filterKeys { it != "form_identifier" }
            .mapValues { (_, value) ->
                when (value) {
                    is String -> {
                        val trimmed = value.trim()
                        if (placeholderValues.any { trimmed.equals(it, ignoreCase = true) }) {
                            null
                        } else if (trimmed.startsWith("Select", ignoreCase = true) || trimmed.isBlank()) {
                            null
                        } else {
                            trimmed
                        }
                    }

                    is List<*> -> if (value.isEmpty()) null else value

                    else -> value
                }
            }
            .filterValues { it != null }

        val requestPayload = SubmitRequest(
            data = filteredEntries,
            form_identifier = formIdentifier
        )

        Log.d("handleSubmit", "Final Payload: $filteredEntries")
        apiSubmitForm(requestPayload, termsCheckBox)
    }
    private val placeholderValues = listOf(
        "Select",
        "Select Country",
        "Select Discipline",
        "Select Category",
        "Select Level",
        "Enter Alternate Country Code",
        "Enter Testscore"
    )
    private fun apiSubmitForm(requestPayload: SubmitRequest, termsCheckBox: CheckBox) {
        if (requestPayload != null) {
            LoginViewModal().signUpFormModalLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer ${CommonUtils.accessToken}",
                requestPayload
            ).observe(viewLifecycleOwner) { response ->
                response?.let { (isSuccess, errorMessage) ->
                    if (isSuccess) {
                        // ✅ Success

                        val staticErrors = getStaticValidationErrors(termsCheckBox)
                        if (staticErrors.isNotEmpty()) {
                            // Show errors & stop navigation
                            val message = staticErrors.joinToString("\n")
                            errorDialogOpen(requireActivity(), message)
                            return@observe // 🚫 stop here, no navigation
                        }
                        Toast.makeText(requireActivity(), "Sign up successfully! Please login to continue.", Toast.LENGTH_SHORT).show()

                        findNavController(binding.root).navigate(R.id.signInFragment)

                    } else {
                        val messageToShow = errorMessage ?: "Failed to submit form. Please try again."
                        handleError(messageToShow,termsCheckBox)
                    }
                }
            }
        }
    }
    private fun handleError(errors: Any?, termsCheckBox: CheckBox) {
        if (errors == null) {
            Log.e("handleError", "No 'errors' field found")
            CommonUtils.toast(requireActivity(), "Unexpected error format")
            return
        }

        val errorMessages = mutableListOf<String>()

        try {
            when (errors) {

                is JSONObject -> {
                    val keys = errors.keys()
                    while (keys.hasNext()) {
                        val key = keys.next().toString() // ✅ ensure String
                        val formattedKey = key.replace("_", " ")
                            .replaceFirstChar { it.uppercaseChar() }

                        when (val value = errors.get(key)) {
                            is JSONArray -> {
                                for (i in 0 until value.length()) {
                                    val msg = value.optString(i)
                                    if (msg.isNotEmpty()) errorMessages.add("$formattedKey: $msg")
                                }
                            }
                            else -> {
                                val msg = value.toString()
                                if (msg.isNotEmpty()) errorMessages.add("$formattedKey: $msg")
                            }
                        }
                    }
                }

                // ✅ Case 2: errors is a JSONArray
                is JSONArray -> {
                    for (i in 0 until errors.length()) {
                        when (val item = errors.get(i)) {
                            is JSONObject -> {
                                val keys = item.keys()
                                while (keys.hasNext()) {
                                    val key = keys.next().toString() // ✅ ensure String
                                    val formattedKey = key.replace("_", " ")
                                        .replaceFirstChar { it.uppercaseChar() }
                                    val msg = item.optString(key)
                                    if (msg.isNotEmpty()) errorMessages.add("$formattedKey: $msg")
                                }
                            }
                            else -> {
                                val msg = item.toString()
                                if (msg.isNotEmpty()) errorMessages.add(msg)
                            }
                        }
                    }
                }

                // ✅ Case 3: errors is just a String or other type
                else -> {
                    val msg = errors.toString()
                    if (msg.isNotEmpty()) errorMessages.add(msg)
                }
            }

            val staticErrors = getStaticValidationErrors(termsCheckBox)
            if (staticErrors.isNotEmpty()) {
                errorMessages.addAll(staticErrors)
            }


            // ✅ Show all combined messages
            val fullMessage = errorMessages.joinToString("\n").trim()
            if (fullMessage.isNotEmpty()) {
                errorDialogOpen(requireActivity(), fullMessage)
                Log.e("API Error", fullMessage)
            } else {
                Log.e("handleError", "No readable error message found")
                CommonUtils.toast(requireActivity(), "Unexpected error format")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("handleError", "Error parsing response: ${e.message}")
            CommonUtils.toast(requireActivity(), "Error parsing response")
        }
    }

    private fun getStaticValidationErrors(termsCheckBox: CheckBox): List<String> {
        val staticErrors = mutableListOf<String>()

        // Example: Checkbox validation
        if (!termsCheckBox.isChecked) {
            staticErrors.add("Please accept Terms and Conditions.")
        }

        // (Optional) If you want to check for other required fields:
        fieldValues.forEach { (key, value) ->
            if (value is String && value.trim().isEmpty()) {
                val formattedKey = key.replace("_", " ").replaceFirstChar { it.uppercaseChar() }
                staticErrors.add("$formattedKey is required.")
            }
        }

        return staticErrors
    }

    private fun createEditText(field: FormField) {
        val container = LinearLayout(requireActivity()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                setMargins(0, resources.getDimensionPixelSize(R.dimen.dp_10), 0, 0)
            }
        }

        val label = TextView(requireActivity()).apply {
            val labelText = field.label?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val spannableLabel =
                SpannableString("$labelText${if (field.validations?.required == true) " *" else ""}")
            if (field.validations?.required == true) {
                spannableLabel.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.red)),
                    labelText!!.length,
                    spannableLabel.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        }

        val editText = EditText(requireActivity()).apply {
            hint = field.placeholder
            inputType = when (field.type) {
                "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
                "number" -> InputType.TYPE_CLASS_NUMBER
                else -> InputType.TYPE_CLASS_TEXT
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
            setPadding(padding, padding, padding, padding)

            addTextChangedListener {

                fieldValues[field.name] = it.toString()

            }
        }
        editTextFields[field.name] = editText

        container.addView(label)
        container.addView(editText)
        binding.formContainer.addView(container)
    }

    private fun createRadioButton(field: FormField) {
        val container = LinearLayout(requireActivity()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, resources.getDimensionPixelSize(R.dimen.dp_10), 0, 0)
            }
        }

        // Add a label for the radio group
        val labelText = field.label?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        val spannableLabel =
            SpannableString("$labelText${if (field.validations?.required == true) " *" else ""}")
        if (field.validations?.required == true) {
            spannableLabel.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.red
                    )
                ), // Red color for the star
                labelText!!.length + 1,
                spannableLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val label = TextView(requireActivity()).apply {
            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(ContextCompat.getColor(context, R.color.black)) // Set label text color
        }

        val radioGroup = RadioGroup(requireActivity()).apply {
            orientation = RadioGroup.VERTICAL
            val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
            setPadding(padding, padding, padding, padding)
        }

        field.field_options?.options?.forEach { option ->
            val radioButton = RadioButton(requireActivity()).apply {
                text = option.label
                id = View.generateViewId()
            }

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    fieldValues[field.name] = option.value
                }
            }
            radioGroup.addView(radioButton)
        }

        container.addView(label)
        container.addView(radioGroup)

        binding.formContainer.addView(container)
    }

    private fun  createSpinner(field: FormField, fields: List<FormField>) {
        val container = LinearLayout(requireActivity()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = resources.getDimensionPixelSize(R.dimen.dp_10) }
        }

        val label = TextView(requireActivity()).apply {
            val labelText = field.label!!.capitalize(Locale.ROOT)
            val spannableLabel =
                SpannableString("$labelText${if (field.validations?.required == true) " *" else ""}")
            spannableLabel.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                labelText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (field.validations?.required == true) {
                spannableLabel.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.red)),
                    labelText.length,
                    spannableLabel.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        val searchableSpinner = SearchableSpinner(requireActivity()).apply {
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            setTitle("${field.label!!.capitalize(Locale.ROOT)}")
            setPositiveButton("Close")

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position) as String
                    fieldValues[field.name] = selectedItem
                    field.assigned_field_options?.children_fields?.forEach { relatedField ->
                        fields.find { it.name == relatedField }?.let { relatedLeadField ->
                            updateDependentSpinner(selectedItem, relatedLeadField)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        container.addView(label)
        container.addView(searchableSpinner)
        binding.formContainer.addView(container)
        dependentSpinners[field.name] = searchableSpinner

        if (field.field_options?.type == "manual") {
            val manualOptions = field.field_options?.options?.map { it.label } ?: emptyList()
            val items =
                mutableListOf(field.placeholder).apply { addAll(manualOptions) }
            setupSpinnerAdapter(searchableSpinner, items)
        } else if (field.field_options?.type == "api") {
            field.field_options?.url?.let { url ->
                fetchDataAndUpdateSpinner(url, searchableSpinner, field)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addDynamicPhoneInputWithCountryCode(field: FormField) {
        val context = requireContext()

        // Create label
        val labelText = field.label?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        val spannableLabel = SpannableString("$labelText${if (field.validations!!.required == true) " *" else ""}")
        if (field.validations!!.required == true) {
            spannableLabel.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
                labelText!!.length,
                spannableLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val phoneLabel = TextView(context).apply {
            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        val inputHeight = resources.getDimensionPixelSize(R.dimen.dp_40)

        // Country Code Picker
        val ccp = com.hbb20.CountryCodePicker(context).apply {
            setAutoDetectedCountry(true)
            showFlag(true)
            showFullName(false)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_country_code_no)
            setContentColor(ContextCompat.getColor(context, R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics
                ).toInt(), inputHeight
            )
        }

        // Phone EditText
        val phoneEditText = EditText(context).apply {
            hint = "Enter phone number"
            inputType = InputType.TYPE_CLASS_PHONE
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_phone_no)
            setPadding(16, 0, 16, 0)
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, inputHeight, 1f)
        }

        // Helper function to keep value updated
        fun updateMobileValue() {
            val code = "+${ccp.selectedCountryCode}"
            val mobile = phoneEditText.text.toString().trim()
            fieldValues[field.name] = "$code-$mobile"
        }

        ccp.setOnCountryChangeListener { updateMobileValue() }

        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateMobileValue()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val phoneContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.dp_8)
            }
            addView(ccp)
            addView(phoneEditText)
        }

        binding.formContainer.addView(phoneLabel)
        binding.formContainer.addView(phoneContainer)
    }
    private fun fetchDataAndUpdateSpinner(
        url: String,
        spinner: Spinner,
        field: FormField
    ) {
        viewModel.getCountryList(
            requireActivity(),
            url,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->

            // Safety check for fragment lifecycle
            if (!isAdded || context == null) return@observe

            if (response?.statusCode == 200 && response.success == true) {
                try {
                    val items: List<UnifiedDropdownItem> = when (val rawData = response.data) {

                        // ✅ Case 1: Direct data array
                        is List<*> -> rawData.mapNotNull { item ->
                            val mapItem = item as? Map<*, *> ?: return@mapNotNull null
                            val label = mapItem["label"]?.toString()?.trim()
                            val value = when (val rawValue = mapItem["value"]) {
                                is Double -> rawValue.toInt().toString()
                                is Float -> rawValue.toInt().toString()
                                is Int -> rawValue.toString()
                                is Long -> rawValue.toString()
                                else -> rawValue?.toString()?.trim()?.removeSuffix(".0")
                            }
                            if (!label.isNullOrEmpty() && !value.isNullOrEmpty()) {
                                UnifiedDropdownItem(value, label)
                            } else null
                        }

                        // ✅ Case 2: data is object with "recordInfo"/"recordsInfo"
                        is Map<*, *> -> {
                            val recordArray = rawData["recordsInfo"] as? List<*>
                                ?: rawData["recordsInfo"] as? List<*>
                                ?: emptyList<Any>()

                            recordArray.mapNotNull { item ->
                                val mapItem = item as? Map<*, *> ?: return@mapNotNull null
                                val label = mapItem["label"]?.toString()?.trim()
                                val value = when (val rawValue = mapItem["value"]) {
                                    is Double -> rawValue.toInt().toString()
                                    is Float -> rawValue.toInt().toString()
                                    is Int -> rawValue.toString()
                                    is Long -> rawValue.toString()
                                    else -> rawValue?.toString()?.trim()?.removeSuffix(".0")
                                }
                                if (!label.isNullOrEmpty() && !value.isNullOrEmpty()) {
                                    UnifiedDropdownItem(value, label)
                                } else null
                            }
                        }

                        else -> {
                            Log.e("DropdownData", "❌ Unexpected data format: $rawData")
                            emptyList()
                        }
                    }

                    // ✅ Add placeholder and set adapter
                    val labels = mutableListOf(field.placeholder).apply {
                        addAll(items.map { it.label })
                    }

                    setupSpinnerAdapter(spinner, labels)
                    spinner.setSelection(0)

                    Log.d(
                        "DropdownData",
                        "✅ Spinner updated for '${field.label}' with ${items.size} items"
                    )

                } catch (e: Exception) {
                    Log.e("DropdownParser", "❌ Parsing error: ${e.localizedMessage}", e)
                    CommonUtils.toast(requireContext(), "Error parsing dropdown data.")
                }

            } else {
                Log.e("SpinnerData", "❌ API Error: ${response?.message}")
                CommonUtils.toast(requireContext(), response?.message ?: "Failed to fetch dropdown data")
            }
        }
    }

    private fun setupSpinnerAdapter(spinner: Spinner, options: List<String?>) {

        spinner.adapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                options
            )
    }

    private fun updateDependentSpinner(selectedOption: String, field: FormField) {
        fieldValues[field.name] = selectedOption

        val url = buildUrlWithParams(field.field_options?.url, field.assigned_field_options?.params)

        dependentSpinners[field.name]?.let { spinner ->
            setupSpinnerAdapter(
                spinner,
                listOf(field.placeholder)
            )

            if (url != null) {

                fetchDataAndUpdateDependentSpinner(url, spinner, field)
            }
        }
    }

    private fun buildUrlWithParams(baseUrl: String?, paramInfo: List<DependentParam>?,): String {
        val params = paramInfo?.joinToString("&") { param ->
            val paramValue = fieldValues[param.form_field_key] ?: ""
            "${param.name}=$paramValue"
        }

        val url = if (baseUrl?.contains("?") == true) {
            "$baseUrl&$params"
        } else {
            "$baseUrl?$params"
        }

        Log.d("BuildUrl", "Generated URL: $url")

        return url
    }

    private fun fetchDataAndUpdateDependentSpinner(
        url: String,
        spinner: Spinner,
        field: FormField
    ) {
        viewModel.getCountryList(
            requireActivity(),
            url,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->

            // ✅ Avoid crash if fragment not attached
            if (!isAdded || context == null) return@observe

            try {
                if (response?.statusCode == 200 && response.success) {

                    val dataArray = response.data as? List<*> ?: emptyList<Any>()

                    val items = dataArray.mapNotNull { entry ->
                        val mapItem = entry as? Map<*, *> ?: return@mapNotNull null
                        val label = mapItem["label"]?.toString()?.trim()
                        val value = when (val rawValue = mapItem["value"]) {
                            is Double -> rawValue.toInt().toString()
                            is Float -> rawValue.toInt().toString()
                            is Int -> rawValue.toString()
                            is Long -> rawValue.toString()
                            else -> rawValue?.toString()?.trim()?.removeSuffix(".0")
                        }

                        if (!label.isNullOrEmpty() && !value.isNullOrEmpty()) {
                            UnifiedDropdownItem(value, label)
                        } else null
                    }

                    val labels = mutableListOf(field.placeholder).apply {
                        addAll(items.map { it.label })
                    }

                    setupSpinnerAdapter(spinner, labels)
                    spinner.setSelection(0)

                    Log.d("DependentSpinner", "✅ Loaded ${items.size} items for ${field.label}")

                } else {
                    Log.e("DependentSpinner", "❌ Error: ${response?.message ?: "Failed to fetch data"}")
                    context?.let {
                        CommonUtils.toast(it, response?.message ?: "Failed to fetch dropdown data")
                    }
                }
            } catch (e: Exception) {
                Log.e("DependentSpinner", "Exception parsing dropdown: ${e.localizedMessage}", e)
                context?.let {
                    CommonUtils.toast(it, "Something went wrong while loading dropdown")
                }
            }
        }
    }
}
