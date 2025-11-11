package com.student.Compass_Abroad

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterAdmissionStatus
import com.student.Compass_Abroad.adaptor.AdapterDestinationCountrySelector
import com.student.Compass_Abroad.databinding.FragmentEducationLoanBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.admissionStatus.AdmissionStatus
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.loanApply.LoanAppliedModal
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.Compass_Abroad.retrofit.ViewModalGhyanDhan


class EducationLoanFragment : BaseFragment() {
    private lateinit var binding: FragmentEducationLoanBinding
    private val arrayListCountry: MutableList<com.student.Compass_Abroad.modal.getDestinationCountryList.Data> = mutableListOf()
    private val arrayListAdmissionStatus: MutableList<com.student.Compass_Abroad.modal.admissionStatus.Data> = mutableListOf()
    var destinationCountry: String? = null
    var admissionStatus: String? = null
    var country_id: String? = null
    var admission_status_id: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEducationLoanBinding.inflate(inflater, container, false)

        getDestinationCountryList(requireActivity(), binding.destinationCountry)

        getAdmissionStatus(requireActivity(), binding.admissionStatus)

        binding.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        onClicks()

        setCurrencySpinner()

        return binding.root
    }

    private fun setCurrencySpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.currency_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {

                    val selectedCurrency = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun onClicks() {
        binding.btnSubmit.setOnClickListener {
           val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.etEmail.text.toString()
            val mobileNumber = binding.etPhone.text.toString()
            val loanAmount = binding.amount.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || mobileNumber.isEmpty() || loanAmount.isEmpty()  || admission_status_id.isNullOrEmpty()) {

                CommonUtils.toast(requireActivity(), "Please fill all the fields")
            }else
            {
               applyLoan(firstName,lastName,email,mobileNumber,loanAmount,admission_status_id!!)
            }

        }

        binding.destinationCountry.setOnClickListener {

            getDestinationCountryList(requireActivity(), binding.destinationCountry)

        }


        binding.admissionStatus.setOnClickListener {

            getAdmissionStatus(requireActivity(), binding.admissionStatus)

        }

    }

    private fun applyLoan(
        firstName: String,
        lastName: String,
        email: String,
        mobileNumber: String,
        loanAmount: String,
        admissionStatusId: String) {
        val fullName = "${firstName.trim()} ${lastName.trim()}"
        ViewModalGhyanDhan().appliedLoad(
            activity = requireActivity(),
            client_number = "admissionycom",
            accessToken = "1c7e99b3f196546128aab7be3a063772",
            name = fullName,
            email = email,
            mobile_number = mobileNumber,
            amount = loanAmount,
            target_country_code = binding.countryCode.selectedCountryNameCode.toString(),
            application_status = admissionStatusId
        ).observe(requireActivity()) { response: LoanAppliedModal? ->
            response?.let {
                if (it.success) {
                    it.data?.let { data -> 
                        CommonUtils.toast(requireActivity(), "Lead Created: ID ${data.lead_id}")
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } ?: run {
                        CommonUtils.toast(requireActivity(), "Data not found. Please try again.")
                    }
                } else {

                    CommonUtils.toast(requireActivity(), it.message)
                }
            } ?: run {
                CommonUtils.toast(requireActivity(), "Something went wrong")
            }
        }
    }


    private fun getAdmissionStatus(
        requireActivity: FragmentActivity,
        admissionStatus: TextView
    ) {
        LoginViewModal().getAdmissionStatus(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { destination: AdmissionStatus? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    getDestinationCountry.data?.let {
                        arrayListAdmissionStatus.addAll(it)
                    } ?: run {
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }

                    setDropDownAdmissionStatus(admissionStatus)
                } else {
                    CommonUtils.toast(requireActivity, getDestinationCountry.message ?: "Failed")
                }
            }
        }
    }

    private fun setDropDownAdmissionStatus(admissionStatus: TextView) {
        admissionStatus.setOnClickListener {
            val dialog = Dialog(requireContext())
            val layout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            dialog.setContentView(layout)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val window = dialog.window
            window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            val layoutParams = window?.attributes
            layoutParams?.y = 100 // distance from top
            window?.attributes = layoutParams
            dialog.setCancelable(true)
            layout.findViewById<EditText>(R.id.etSelect).hint = "Search"
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterAdmissionStatus(requireActivity(), arrayListAdmissionStatus, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedItem ->
                admissionStatus.text = selectedItem.label
                admission_status_id = selectedItem.label
                dialog.dismiss()
            }

            // Filter on text change
            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        adapter.getFilter().filter(s.toString())
                    }
                })

            dialog.show()
        }
    }
    private fun getDestinationCountryList(
        requireActivity: FragmentActivity,
        et_destination_Country: TextView) {
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
            val dialog = Dialog(requireContext())
            val layout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            dialog.setContentView(layout)

            // Optional: transparent background
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val window = dialog.window
            window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            val layoutParams = window?.attributes
            layoutParams?.y = 100
            window?.attributes = layoutParams

            dialog.setCancelable(true)

            layout.findViewById<EditText>(R.id.etSelect).hint = "Search Country"

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterDestinationCountrySelector(requireActivity(), arrayListCountry, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                et_destination_Country.text = selectedCountry.label
                country_id = selectedCountry.label
                dialog.dismiss()
            }

            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        adapter.getFilter().filter(s.toString())
                    }
                })

            dialog.show()
        }
    }


    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible = false

    }
}