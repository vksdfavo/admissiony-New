@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
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
import android.view.WindowInsetsController
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
import androidx.databinding.adapters.ViewBindingAdapter.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.student.Compass_Abroad.ApiResponseForm
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.LeadField
import com.student.Compass_Abroad.ParamInfo
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.databinding.FragmentSignUpBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.PrivacyPolicyFragment
import com.student.Compass_Abroad.fragments.TermsAndConditionsFragment
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.submitSinUp.SubmitSinUpForm
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale
import kotlin.random.Random

class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: ViewModalClass
    private val dependentSpinners: MutableMap<String, Spinner> = mutableMapOf()
    private val fieldValues: MutableMap<String, String> = mutableMapOf()
    var contentKey = ""
    var referralCode: String? = null
    var statusValidation: Int? = null
    private val editTextFields: MutableMap<String, EditText> = mutableMapOf()
    private var isFirstTimeErrorHandled = false
    private var requiredFieldsArray: JSONArray? = null
    val list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ViewModalClass::class.java]

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.teall)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
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

        statusValidation = App.singleton!!.statusValidation

        binding.back.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        referralCode = arguments?.getString("referral")



        fetchLeadForm()

        return binding.root
    }

    private fun fetchLeadForm() {
        viewModel.leadFormResponseLiveData(requireActivity(), AppConstants.fiClientNumber)
            .observe(requireActivity()) { leadFormResponse: ApiResponseForm? ->
                leadFormResponse?.let { response ->
                    if (response.statusCode == 200 && response.success) {
                        val formName = response.data?.name ?: "Submit"

                        createDynamicForm(response.data?.lead_fields ?: emptyList(), formName)

                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            response.message ?: "Failed to fetch form"
                        )
                    }
                }
            }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun createDynamicForm(fields: List<LeadField>, formName: String) {
        binding.formContainer.removeAllViews()

        fields.sortedBy { it.lead_form_field.order_by }.forEach { field ->
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
            }
        }

        val referralCode = arguments?.getString("referral") ?: ""

        val referralLabelText = if (referralCode.isNullOrEmpty()) {
            "Do you have any referral code?*"
        } else {
            "Referral Code"
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

        val referralLabel = TextView(requireContext()).apply {
            text = spannableLabel
            textSize = 14f
            setTextColor(Color.BLACK)
            gravity = Gravity.START
            setPadding(0, resources.getDimensionPixelSize(R.dimen.dp_10), 0, 0)
            typeface = Typeface.DEFAULT_BOLD
        }


// Space after radio buttons
        val spaceAfterRadioButtons = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.dp_8)
            )
        }

// Referral EditText
        val referralEditText = EditText(requireContext()).apply {
            hint = "Enter Referral Code"
            setHintTextColor(Color.GRAY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
            setPadding(padding, padding, padding, padding)
            background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.shape_rectangle_all_radius_et_login
            )
        }

        if (referralCode.isNotEmpty()) {
            referralEditText.setText(referralCode)
        } else {

        }

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        when (currentFlavor) {
            "admisiony", "firmli","eeriveurope", "unitedglobalservices" -> {
                binding.formContainer.addView(referralLabel)
                 binding.formContainer.addView(spaceAfterRadioButtons)
                binding.formContainer.addView(referralEditText)


            }
        }


        // Parent layout to hold checkbox and text side by side or vertically
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

// 3. Create TextView for clickable terms
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

// Add views to the container layout
        containerLayout.addView(termsCheckBox)
        containerLayout.addView(termsTextView)

// Finally, add to your form container
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


        if (statusValidation == 1) {
            handleSubmit(termsCheckBox, referralCode)
        } else if (statusValidation == 0) {
            // Do nothing or show a message if needed
        }

        val submitButton = Button(requireActivity()).apply {
            text = formName
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_signup_button)
            setTextColor(Color.WHITE)
            setOnClickListener {

                handleSubmit(termsCheckBox, referralCode)
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


    private fun handleSubmit(termsCheckBox: CheckBox?, referralCode1: String) {
        var isValid = true
        var enteredEmail: String? = null
        val isMaven = BuildConfig.FLAVOR.equals("MavenConsulting", ignoreCase = true)

        val originalCountryCode = fieldValues["country_code"] ?: ""
        val extractedCountryCode = if (originalCountryCode.contains(" - ")) {
            originalCountryCode.split(" - ")[0].trim().removePrefix("+")
        } else {
            ""
        }

        val originalAlternativeCountryCode = fieldValues["alternate_country_code"] ?: ""
        val extractedAlternativeCountryCode = if (originalAlternativeCountryCode.contains(" - ")) {
            originalAlternativeCountryCode.split(" - ")[0].trim().removePrefix("+")
        } else {
            ""
        }



        list.clear()

        if (requiredFieldsArray != null) {
            for (i in 0 until requiredFieldsArray!!.length()) {
                val errorObject = requiredFieldsArray!!.getJSONObject(i)
                val field = errorObject.keys().next().toString()
                list.add(field)
            }


            val errors = mutableListOf<String>()

            for (field in list) {
                val value = fieldValues[field]
                Log.d("FieldValidation", "Field: $field, Value: $value")

                if (value == null ||
                    (value is String && (value.isBlank() || value.startsWith("Select") || value.startsWith(
                        "Country"
                    ))) ||
                    (value is ArrayList<*> && value.isEmpty())
                ) {
                    val fieldName = field.replace("_", " ").replaceFirstChar { it.uppercase() }
                    errors.add("$fieldName is required.")
                }
            }

            if (termsCheckBox?.isChecked != true) {
                errors.add("Please accept Terms and conditions")
            }


            val unwantedMessages = listOf("Message is required.")
            val filteredErrors =
                errors.filterNot { it.contains(unwantedMessages[0], ignoreCase = true) }

            if (filteredErrors.isNotEmpty()) {
                errorDialogOpen(requireActivity(), filteredErrors.joinToString("\n"))
                return
            } else {
                Log.d("apiSubmitSignUps", "No important error to show.")
            }

            for ((fieldName, editText) in editTextFields) {
                val value = editText.text.toString()
                if (fieldName.contains("email", ignoreCase = true)) {
                    enteredEmail = value
                    break
                }
            }

            enteredEmail?.let {

                sharedPre?.saveString(AppConstants.USER_EMAIL, it)

            }

            if (isValid) {
                val hexString = generateRandomHexString(16)
                val publicKey = hexString
                val privateKey = AppConstants.privateKey
                val appSecret = AppConstants.appSecret
                val ivHexString = "$privateKey$publicKey"

                val formData = JSONObject()

                try {
                    fieldValues.forEach { (key, value) ->
                        val stringValue = value as? String ?: ""
                        val finalValue = if (stringValue.startsWith("Select")) null else stringValue
                        formData.put(key, finalValue)
                    }

                    if (!referralCode1.isNullOrEmpty()) {
                        formData.put("referral_identifier", referralCode1)
                    }

                    formData.put("lead_form_identifier", "LFO1716112595567K56USAHJ02")
                    formData.put("country_code", extractedCountryCode)
                    if (isMaven) {
                        formData.put("alternate_country_code", extractedAlternativeCountryCode)
                    }

                    sharedPre?.saveString(AppConstants.publicKey, formData.toString())
                    Log.d("loginUser", formData.toString())

                    val encryptedString = encryptData(formData.toString(), appSecret, ivHexString)
                    if (encryptedString != null) {
                        contentKey = "$publicKey^#^$encryptedString"
                        Log.d("loginUser", contentKey)

                        //apiSubmitSignUp(contentKey, publicKey, termsCheckBox)
                    } else {
                        Log.e("loginUser", "Encryption failed.")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireActivity(),
                        "Failed to prepare form data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {


            for ((fieldName, editText) in editTextFields) {
                val value = editText.text.toString()
                if (fieldName.contains("email", ignoreCase = true)) {
                    enteredEmail = value
                    break
                }
            }

            enteredEmail?.let {
                sharedPre?.saveString(AppConstants.USER_EMAIL, it)
            }



            if (isValid) {
                val hexString = generateRandomHexString(16)
                val publicKey = hexString
                val privateKey = AppConstants.privateKey
                val appSecret = AppConstants.appSecret
                val ivHexString = "$privateKey$publicKey"

                val formData = JSONObject()

                try {
                    fieldValues.forEach { (key, value) ->
                        val stringValue = value as? String ?: ""
                        val finalValue = if (stringValue.startsWith("Select")) null else stringValue
                        formData.put(key, finalValue)
                    }

                    if (!referralCode1.isNullOrEmpty()) {
                        formData.put("referral_identifier", referralCode1)
                    }

                    formData.put("lead_form_identifier", "LFO1716112595567K56USAHJ02")
                    formData.put("country_code", extractedCountryCode)
                    if (isMaven) {
                        formData.put("alternate_country_code", extractedAlternativeCountryCode)
                    }

                    sharedPre?.saveString(AppConstants.publicKey, formData.toString())
                    Log.d("loginUser", formData.toString())

                    val encryptedString = encryptData(formData.toString(), appSecret, ivHexString)
                    if (encryptedString != null) {
                        contentKey = "$publicKey^#^$encryptedString"
                        Log.d("loginUser", contentKey)

                       // apiSubmitSignUp(contentKey, publicKey, termsCheckBox)
                    } else {
                        Log.e("loginUser", "Encryption failed.")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireActivity(),
                        "Failed to prepare form data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun apiSubmitSignUp(
        encrytedContent: String?,
        publicKey: String,
        termsCheckBox: CheckBox?
    ) {
        if (encrytedContent != null) {
            LoginViewModal().signUpFormModalLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                contentKey
            ).observe(requireActivity()) { submitSinUpForm: SubmitSinUpForm? ->
                submitSinUpForm?.let { response ->
                    if (response.statusCode == 201) {

                        val encryptedContent = CommonUtils.createContentCheckUser(
                            requireActivity(),
                            publicKey,
                            response.data?.userInfo?.email,
                            "signup"
                        )

                        apiCheckUser(encryptedContent, publicKey)

                    }
                    if (response.statusCode == 422) {
                        if (response.errors != null) {
                            if (response.errors is JSONArray) {
                                requiredFieldsArray = response.errors
                                Log.e("Error", "Expected JSONArray but got: ${response.errors}")
                                if (isFirstTimeErrorHandled) {

                                    handleError(requiredFieldsArray)

                                } else {
                                    isFirstTimeErrorHandled = true
                                }
                            } else {
                                Log.e(
                                    "Error",
                                    "Expected JSONArray but got: ${response.errors?.javaClass?.name}"
                                )
                            }
                        } else {
                            val errorMessage = response.message ?: "An unknown error occurred."
                            //CommonUtils.toast(this, errorMessage)
                        }
                    }


                }
            }
        } else {

            Toast.makeText(requireActivity(), "Failed to prepare form data", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun handleError(errorsArray: JSONArray?) {
        if (errorsArray != null && errorsArray.length() > 0) {
            val errorMessages = StringBuilder()

            for (i in 0 until errorsArray.length()) {
                val firstError = errorsArray.getJSONObject(i)
                val keys = firstError.keys()

                while (keys.hasNext()) {
                    val key = keys.next()
                    val keyStr = key.toString()
                    val value = firstError.optString(keyStr, "")

                    val fieldName = keyStr.replace("_", " ").replaceFirstChar {

                        it.toString().uppercase(Locale.getDefault())

                    }

                    errorMessages.append("$fieldName: $value\n")
                }

            }

            val fullErrorMessage = errorMessages.toString().trim()
            if (fullErrorMessage.isNotEmpty()) {
                errorDialogOpen(requireActivity(), fullErrorMessage)
                Log.d("apiSubmitSignUp", fullErrorMessage)
            } else {
                Log.d("handleError", "No important error to show in popup.")
            }
        } else {
            Log.e("ErrorParsing", "Error response does not contain 'errors' array or is empty")
            CommonUtils.toast(activity, "Unexpected error format")
        }
    }


    private fun apiCheckUser(content: String?, publicKey: String) {
        contentKey = "$publicKey^#^$content"

        LoginViewModal().checkUserModelLiveData(requireActivity(), contentKey)
            .observe(viewLifecycleOwner) { loginModal: CheckUserModel? ->
                loginModal?.let { nonNullLoginModal ->
                    if (nonNullLoginModal.statusCode == 200) {

                        if (loginModal.data?.oneTimePasswordInfo?.p_set == false || loginModal.data?.oneTimePasswordInfo?.v_set == false) {
                            findNavController(binding.root)
                                .navigate(R.id.verifyOtpFragment)
                            requiredFieldsArray = JSONArray()
                            list.clear()

                            with(sharedPre!!) {

                                saveString(
                                    AppConstants.OTP_IDENTIFIER,
                                    loginModal.data!!.oneTimePasswordInfo.identifier
                                )
                                saveString(
                                    AppConstants.User_IDENTIFIER,
                                    loginModal.data!!.userInfo.identifier
                                )
                                saveString(
                                    AppConstants.Device_IDENTIFIER,
                                    loginModal.data!!.userDeviceInfo.identifier
                                )
                            }

                        } else {

                            findNavController(
                                binding
                                    .getRoot()
                            )
                                .navigate(R.id.passwordFragment)

                        }

                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullLoginModal.message ?: "Forget Failed"
                        )
                    }
                }
            }
    }

    @SuppressLint("ResourceType")
    private fun createEditText(field: LeadField) {
        val container = LinearLayout(requireActivity()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                setMargins(0, resources.getDimensionPixelSize(R.dimen.dp_20), 0, 0)
            }
        }

       /* val label = TextView(requireActivity()).apply {
            val labelText = field.label.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val spannableLabel =
                SpannableString("$labelText${if (field.is_required == 1) " *" else ""}")
            if (field.is_required == 1) {
                spannableLabel.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.red)),
                    labelText.length,
                    spannableLabel.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        }*/

        val editText = EditText(requireActivity()).apply {
            hint = field.lead_form_field.placeholder
            inputType = when (field.type) {
                "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
                "number" -> InputType.TYPE_CLASS_NUMBER
                else -> InputType.TYPE_CLASS_TEXT
            }
            setHintTextColor(Color.GRAY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
            setPadding(padding, padding, padding, padding)

            addTextChangedListener {

                fieldValues[field.name] = it.toString()

            }
        }
        editTextFields[field.name] = editText

        //container.addView(label)
        container.addView(editText)
        binding.formContainer.addView(container)
    }

    private fun createRadioButton(field: LeadField) {
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
        val labelText = field.label.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        val spannableLabel =
            SpannableString("$labelText${if (field.is_required == 1) " *" else ""}")
        if (field.is_required == 1) {
            spannableLabel.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.red
                    )
                ), // Red color for the star
                labelText.length + 1,
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

        field.options_data?.options?.forEach { option ->
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

    private fun createSpinner(field: LeadField, fields: List<LeadField>) {
        val container = LinearLayout(requireActivity()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = resources.getDimensionPixelSize(R.dimen.dp_10) }
        }

        /*val label = TextView(requireActivity()).apply {
            val labelText = field.label.capitalize(Locale.ROOT)
            val spannableLabel =
                SpannableString("$labelText${if (field.is_required == 1) " *" else ""}")
            spannableLabel.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                labelText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (field.is_required == 1) {
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
        }*/

        val searchableSpinner = SearchableSpinner(requireActivity()).apply {
            setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            setTitle("${field.label.capitalize(Locale.ROOT)}")
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
                    field.options_data?.related_fields?.forEach { relatedField ->
                        fields.find { it.name == relatedField }?.let { relatedLeadField ->
                            updateDependentSpinner(selectedItem, relatedLeadField)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        //container.addView(label)
        container.addView(searchableSpinner)
        binding.formContainer.addView(container)
        dependentSpinners[field.name] = searchableSpinner

        if (field.options_data?.type == "manual") {
            val manualOptions = field.options_data?.options?.map { it.label } ?: emptyList()
            val items =
                mutableListOf(field.lead_form_field.placeholder).apply { addAll(manualOptions) }
            setupSpinnerAdapter(searchableSpinner, items)
        } else if (field.options_data?.type == "api") {
            field.options_data?.url?.let { url ->
                fetchDataAndUpdateSpinner(url, searchableSpinner, field)
            }
        }
    }

    private fun fetchDataAndUpdateSpinner(url: String, spinner: Spinner, field: LeadField) {
        viewModel.getCountryList(
            requireActivity(),
            url,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity()) { response ->
            response?.data?.map { it.label }?.let { options ->
                val items =
                    mutableListOf(field.lead_form_field.placeholder).apply { addAll(options) }
                if (isAdded) {
                    setupSpinnerAdapter(spinner, items)

                }
                spinner.setSelection(0)
            }
        }
    }

    private fun setupSpinnerAdapter(spinner: Spinner, options: List<String>) {
        val adapter = object : ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            options
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    (view as TextView).setTextColor(Color.BLACK)
                }
                val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
                (view as TextView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                (view as TextView).setPadding(padding, padding, padding, padding)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    (view as TextView).setTextColor(Color.BLACK)
                }
                (view as TextView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
                (view as TextView).setPadding(padding, padding, padding, padding)
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


    private fun updateDependentSpinner(selectedOption: String, field: LeadField) {
        fieldValues[field.name] = selectedOption

        val url = buildUrlWithParams(field.options_data?.url, field.options_data?.param_info)

        dependentSpinners[field.name]?.let { spinner ->
            setupSpinnerAdapter(
                spinner,
                listOf(field.lead_form_field.placeholder)
            )

            if (url != null) {

                fetchDataAndUpdateDependentSpinner(url, spinner, field)
            }
        }
    }

    private fun buildUrlWithParams(baseUrl: String?, paramInfo: List<ParamInfo>?): String {
        val params = paramInfo?.joinToString("&") { param ->
            val paramValue = fieldValues[param.field_name] ?: ""
            "${param.param_name}=$paramValue"
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
        field: LeadField
    ) {
        viewModel.getCountryList(
            requireActivity(),
            url,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken
        )
            .observe(requireActivity()) { response ->
                response?.let { formAllFieldResponse ->
                    if (formAllFieldResponse.statusCode == 200 && formAllFieldResponse.success) {
                        val options =
                            formAllFieldResponse.data?.map { it.label }?.toMutableList()
                                ?: mutableListOf()
                        options.add(
                            0,
                            field.lead_form_field.placeholder
                        )

                        setupSpinnerAdapter(spinner, options)

                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            formAllFieldResponse.message ?: "Failed"
                        )
                    }
                }
            }
    }

    private fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }
            .joinToString("")

    }
}
