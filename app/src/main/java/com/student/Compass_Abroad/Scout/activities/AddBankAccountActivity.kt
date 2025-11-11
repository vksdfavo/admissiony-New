@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.Scout.activities

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.modalClass.BankAccountRequest
import com.student.Compass_Abroad.Scout.modalClass.BankInfoRequest
import com.student.Compass_Abroad.Scout.modalClass.getBankForm.BankField
import com.student.Compass_Abroad.Scout.modalClass.getBankForm.GetBankAccountForm
import com.student.Compass_Abroad.Scout.retrofitScout.ViewModalScout
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.adaptor.AdapterDestinationCountrySelector
import com.student.Compass_Abroad.databinding.ActivityAddbankAccountBinding
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import java.util.Locale
import kotlin.collections.set

class AddBankAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddbankAccountBinding
    private val arrayListCountry: MutableList<com.student.Compass_Abroad.modal.getDestinationCountryList.Data> =
        mutableListOf()
    private val fieldValues: MutableMap<String, String> = mutableMapOf()
    private val editTextFields: MutableMap<String, EditText> = mutableMapOf()
    var identifier: String = ""
    var country_identifier: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddbankAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDestinationCountryList(this, binding.country)

        window.statusBarColor = getColor(R.color.white)
        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        onClicks()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }


    }
    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    private fun onClicks() {
        binding.fabBiBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        binding.country.setOnClickListener {

            getDestinationCountryList(this, binding.country)

        }

        binding.addCountry.setOnClickListener {

            getDestinationCountryList(this, binding.country)


        }

        binding.btnAddAccount.setOnClickListener {
            fieldValues.forEach { (key, value) ->
                Log.d("FormData", "$key: $value")
            }

            val bankInfo = BankInfoRequest(
                accountName = fieldValues  ["account_name"] ?:     "",
                accountNumber = fieldValues["account_number"] ?:   "",
                bankName = fieldValues     ["bank_name"] ?:        "",
                bankAddress = fieldValues  ["bank_address"] ?:     "",
                swiftCode = fieldValues    ["swift_code"] ?:       ""
            )

            val request = BankAccountRequest(
                countryIdentifier = country_identifier ?: "",
                isPrimary = true,
                info = bankInfo
            )

            ViewModalScout().addBankAccountFormListLiveData(
                this,
                "Bearer " + CommonUtils.accessToken,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "BFO1730874681V942PC6C54",
                request
            ).observe(this) { response ->
                response?.let {
                    if (it.statusCode == 201 && it.success) {

                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        Toast.makeText(
                            this,
                            it.message ?: "Failed to add account",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun fetchBankForm(value: String) {
        ViewModalScout().getBankAccountFormListLiveData(
            this, AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, value
        ).observe(this) { leadFormResponse: GetBankAccountForm? ->
            leadFormResponse?.let { response ->
                if (response.statusCode == 200 && response.success) {


                    binding.llFpApNoData.visibility = View.GONE
                    binding.btnAddAccount.visibility = View.VISIBLE


                    createDynamicForm(response.data?.fields ?: emptyList())

                } else {
                    binding.llFpApNoData.visibility = View.VISIBLE
                    binding.btnAddAccount.visibility = View.GONE

                    CommonUtils.toast(
                        this, response.message ?: "Failed to fetch form"
                    )
                }
            }
        }
    }

    private fun createDynamicForm(fields: List<BankField>) {

        fields.sortedBy { it.bank_information_form_field.order_by }.forEach { field ->
            when (field.type) {

                "text", "email", "number" -> createEditText(field)
            }
        }

    }

    private fun createEditText(field: BankField) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                setMargins(0, resources.getDimensionPixelSize(R.dimen.dp_20), 0, 0)
            }
        }

        val label = TextView(this).apply {
            val labelText = field.label.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val spannableLabel =
                SpannableString("$labelText${if (field.is_required == 1) " *" else ""}")
            if (field.is_required == 1) {
                spannableLabel.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@AddBankAccountActivity,
                            R.color.red
                        )
                    ),
                    labelText.length,
                    spannableLabel.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            text = spannableLabel
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(this@AddBankAccountActivity, R.color.black))
        }

        val editText = EditText(this).apply {
            hint = field.bank_information_form_field.placeholder
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
    private fun getDestinationCountryList(requireActivity: FragmentActivity, et_destination_Country: TextView) {
        ViewModalScout().getAllCountryListLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, identifier)!!,
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
            val popupWindow = PopupWindow(this)
            val layout: View =
                LayoutInflater.from(this).inflate(R.layout.custom_popup2, null)
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

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter =
                AdapterDestinationCountrySelector(this, arrayListCountry, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->

                binding.formContainer.removeAllViews()
                fieldValues.clear()
                editTextFields.clear()

                et_destination_Country.text = selectedCountry.label
                country_identifier = selectedCountry.value

                fetchBankForm(selectedCountry.value)

                Log.d("setDropDownDestinationCountryList", selectedCountry.value)
                popupWindow.dismiss()

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
}