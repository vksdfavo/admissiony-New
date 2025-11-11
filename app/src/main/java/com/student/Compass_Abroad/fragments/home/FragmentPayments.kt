package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterFragmentPayments
import com.student.Compass_Abroad.adaptor.AdapterModeOfPaymentApplicationSelector
import com.student.Compass_Abroad.adaptor.AdapterPaymentForApplicationSelector
import com.student.Compass_Abroad.databinding.FragmentPaymentsBinding
import com.student.Compass_Abroad.modal.getPaymentForDropDown.getPaymentForDropDown
import com.student.Compass_Abroad.modal.getPaymentMode.getPaymentMode
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.databinding.ItempaymentotherBinding
import com.student.Compass_Abroad.modal.getPaymentApplicationPay.Data
import com.razorpay.Checkout
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getPaymentApplication.Record
import org.json.JSONObject


class FragmentPayments : BaseFragment(){

    private lateinit var binding: FragmentPaymentsBinding
    private lateinit var adapterFragmentPayments: AdapterFragmentPayments
    private lateinit var bottomSheetDialog:BottomSheetDialog
    private var currentPage = 1
    private var isLastPage = false
    private val perPage = 10
    var payment_type_identifier=""
    var payment_gateway_identifier=""
    var secretKey:String=""
    private lateinit var paymentSheet: PaymentSheet
    private val leadPaymentList: MutableList<com.student.Compass_Abroad.modal.getPaymentApplication.Record> = mutableListOf()
    private val paymentForList: MutableList<com.student.Compass_Abroad.modal.getPaymentForDropDown.RecordsInfo> = mutableListOf()
    private val modeOfPaymentList: MutableList<com.student.Compass_Abroad.modal.getPaymentMode.Record> = mutableListOf()

    companion object {

        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        initPayment()
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        setRecylerview()
        fetchDataFromApi(currentPage)

        getPaymentForDropdown(requireActivity())
        getModeOfPaymentDropdown(requireActivity())

        setClickListeners()


        return binding.root
    }



    private fun initPayment() {
        paymentSheet = PaymentSheet(
            this,
            PaymentSheetResultCallback { paymentSheetResult: PaymentSheetResult? ->
                paymentSheetResult?.let {
                    onPaymentResult(
                        it
                    )
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDataFromApi(page: Int) {
        if (page == 1) {
            binding.pbFt.visibility = View.VISIBLE
        } else {
            binding.pbFtPagination.visibility = View.VISIBLE
        }


        data?.let {
            ViewModalClass().getPaymentApplicationLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                it.identifier,
                page,
                perPage,
                "desc",
                "id"
            ).observe(viewLifecycleOwner) { response ->
                response?.let {
                    if (it.statusCode == 200 && it.success) {
                        val getPayment = it.data?.records ?: emptyList()
                        val metaInfo = it.data?.metaInfo

                        // Log API response
                        Log.d("Pagination", "API Response: ${response.data}")

                        // If it's the first page, clear the list
                        if (page == 1) {
                            leadPaymentList.clear()
                        }

                        // Add new data to the existing list
                        leadPaymentList.addAll(getPayment)
                        adapterFragmentPayments.notifyDataSetChanged()

                        // Log the fetched data size
                        Log.d(
                            "Pagination",
                            "Fetched ${getPayment.size} items. Total Items: ${leadPaymentList.size}"
                        )

                        // Check if it's the last page based on the meta information
                        isLastPage = !(metaInfo?.hasNextPage ?: false)

                        // Log current page and if there's a next page
                        Log.d(
                            "Pagination",
                            "Current Page: $page, Has Next Page: ${metaInfo?.hasNextPage}"
                        )

                        // Show appropriate views based on data availability
                        if (leadPaymentList.isEmpty()) {
                            binding.llFtNoTransactions.visibility = View.VISIBLE
                            binding.rvFt.visibility = View.GONE
                        } else {
                            binding.llFtNoTransactions.visibility = View.GONE
                            binding.rvFt.visibility = View.VISIBLE
                        }

                        // Check if it's the last page and show a toast message
                        if (isLastPage) {
                            //Toast.makeText(requireContext(), "End of data reached", Toast.LENGTH_SHORT).show()
                            binding.pbFt.visibility = View.GONE
                            binding.pbFtPagination.visibility = View.GONE
                        }
                    } else {
                        // Handle API error response
                        CommonUtils.toast(requireContext(), it.message ?: "Failed")
                        if (page == 1) {
                            binding.llFtNoTransactions.visibility = View.VISIBLE
                            binding.rvFt.visibility = View.GONE
                        }
                    }
                } ?: run {
                    // Handle null response scenario
                    if (page == 1) {
                        binding.llFtNoTransactions.visibility = View.VISIBLE
                        binding.rvFt.visibility = View.GONE
                    }
                }

                // Hide progress indicator after data load completes
                if (page == 1) {
                    binding.pbFt.visibility = View.GONE
                } else {
                    binding.pbFtPagination.visibility = View.GONE
                }
            }
        }
    }


    private fun setRecylerview() {
        if (paymentSheet == null) {
            Log.e("FragmentPayments", "PaymentSheet is null in setRecylerview")
            return
        }

        adapterFragmentPayments = AdapterFragmentPayments(requireActivity(), leadPaymentList, paymentSheet!!,object :AdapterFragmentPayments.select{
            override fun click(
                record: FragmentActivity,
                identifier: String,
                wisePayment: Record,
                binding: ItempaymentotherBinding,
                s1: String
            ) {


            }

            override fun click(
                context: FragmentActivity,
                identifier: String,
                binding: ItempaymentotherBinding,
                s1: String,
            ) {
                calltheApiPay(context,identifier,binding,s1)
            }

        })
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFt.layoutManager = layoutManager
        binding.rvFt.adapter = adapterFragmentPayments

        // Implementing RecyclerView Pagination
        binding.rvFt.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLastPage) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    // Log scrolling info
                    Log.d("Pagination", "Visible Items: $visibleItemCount, Total Items: $totalItemCount, First Visible Item Position: $firstVisibleItemPosition")

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        // Log page increment
                        Log.d("Pagination", "Fetching next page: ${currentPage + 1}")
                        currentPage++
                        fetchDataFromApi(currentPage)
                    }
                }
            }
        })
    }

    private fun getPaymentForDropdown(
        requireActivity: FragmentActivity,
    ) {

        ViewModalClass().getPaymentForDropDownApplicationLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { getPaymentForDropDown: getPaymentForDropDown? ->
            getPaymentForDropDown?.let { nonNullcategoriesResponse ->
                if (getPaymentForDropDown.statusCode == 200) {

                    if (getPaymentForDropDown.data != null) {
                        for (i in 0 until getPaymentForDropDown?.data?.recordsInfo!!.size) {
                            paymentForList?.add(getPaymentForDropDown.data!!.recordsInfo[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve testScore. Please try again."
                        );
                    }



                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getPaymentForDropDown.message ?: " Failed"
                    )
                }
            }
        }

    }

    private fun getModeOfPaymentDropdown(
        requireActivity: FragmentActivity,
    ) {

        ViewModalClass().getModeOFPaymentDropDownApplicationLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { getPaymentModeDropDown: getPaymentMode? ->
            getPaymentModeDropDown?.let { nonNullcategoriesResponse ->
                if (getPaymentModeDropDown.statusCode == 200) {

                    if (getPaymentModeDropDown.data != null) {
                        for (i in 0 until getPaymentModeDropDown?.data?.records!!.size) {
                            modeOfPaymentList?.add(getPaymentModeDropDown.data!!.records[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve testScore. Please try again."
                        );
                    }



                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getPaymentModeDropDown.message ?: " Failed"
                    )
                }
            }
        }

    }



    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    private fun setClickListeners() {
        binding.fabVdAdd.setOnClickListener {

            var bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_paymentlink, null)
            bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheet2)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
            bottomSheetView.minimumHeight = resources.displayMetrics.heightPixels


            var imageView: ImageView
            var paymentForSpinner:TextView
            var currencySpinner:Spinner
            var paymentModeSpinner:TextView
            var generateLink:TextView
              var Price:TextView
              var currency:Spinner


            imageView= bottomSheetView.findViewById(R.id.back_btn_1)
            paymentForSpinner= bottomSheetView.findViewById(R.id.payment_for)
            currencySpinner= bottomSheetView.findViewById(R.id.currency_data)
            paymentModeSpinner= bottomSheetView.findViewById(R.id.payment_mode)
            generateLink= bottomSheetView.findViewById(R.id.tvSp2_save)
            Price=bottomSheetView.findViewById(R.id.tv_amount)



            paymentSpinner(paymentForSpinner)
            CurrencySpinner(currencySpinner)
            PaymentModeSpinner(paymentModeSpinner)

            imageView.setOnClickListener {
                bottomSheetDialog.dismiss()
            }


            generateLink.setOnClickListener { v:View->


               var Pricee=Price.text.toString()
                var payment_type_identifier=payment_type_identifier.toString()
                    var payment_gateway_identifier=payment_gateway_identifier.toString()


                if(payment_type_identifier.isEmpty()){
                    Toast.makeText(requireActivity(),"Please select a Payment For",Toast.LENGTH_SHORT).show()
                }
                else if(currencySpinner.selectedItem.toString().equals("Select Currency")){
                    Toast.makeText(requireActivity(),"Please select a Currency",Toast.LENGTH_SHORT).show()
                }
                else if(Pricee.isEmpty() || Pricee.toDoubleOrNull() ?: 0.0 <= 0.0) {
                 Toast.makeText(requireActivity(),"Please enter a valid amount greater than 0",Toast.LENGTH_SHORT).show()
                }
                else if(payment_gateway_identifier.isEmpty()){
                    Toast.makeText(requireActivity(),"Please select a Mode of Payment",Toast.LENGTH_SHORT).show()
                }
                else {
                   generatingPaymentLinkApplication(requireActivity(),Pricee,currencySpinner.selectedItem.toString(),payment_type_identifier,payment_gateway_identifier)
                }
            }

        }
        }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun PaymentModeSpinner(paymentSpinner: TextView) {


        var fragment: Fragment? = null
        var xLocationOfView = 0
        var yLocationOfView = 0

        paymentSpinner?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Payment Mode")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = paymentSpinner.width

            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                paymentSpinner.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + paymentSpinner?.height!!
            } else {
                // If not in a fragment, use activity view for positioning
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + paymentSpinner?.height!!
            }

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterModeOfPaymentApplicationSelector(requireActivity(),modeOfPaymentList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedModeOfPayment ->
                paymentSpinner?.text = selectedModeOfPayment.name
                payment_gateway_identifier=selectedModeOfPayment.identifier
                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }
// Show the popup window
            popupWindow.showAsDropDown(paymentSpinner)


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

    @RequiresApi(Build.VERSION_CODES.P)
    private fun paymentSpinner(paymentForSpinner: TextView) {
         var fragment: Fragment? = null
         var xLocationOfView = 0
         var yLocationOfView = 0

        paymentForSpinner?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Payment For")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = paymentForSpinner.width
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                paymentForSpinner?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + paymentForSpinner?.height!!
            } else {
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + paymentForSpinner?.height!!
            }

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterPaymentForApplicationSelector(requireActivity(),paymentForList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCampus ->
                paymentForSpinner?.text = selectedCampus.label
                payment_type_identifier=selectedCampus.value

                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(paymentForSpinner)

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

    private fun CurrencySpinner(currencySpinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.select_spinner,
            R.layout.custom_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        currencySpinner.adapter = adapter
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                currencySpinner.setSelection(0)
            }
        }
    }

    private fun generatingPaymentLinkApplication(
        requireActivity: FragmentActivity,
        Pricee: String,
        currencyy: String,
        payment_type_identifier: String,
        payment_gateway_identifier: String) {
            CommonUtils.accessToken?.let {
                data?.let { it1 ->
                    ViewModalClass().genratingPaymentLinkApplicationLiveData(
                        requireActivity,
                        AppConstants.fiClientNumber,
                        App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                        "Bearer " + it,
                        "application",
                        it1.identifier,
                        Pricee,
                        currencyy,
                        payment_type_identifier,
                        payment_gateway_identifier
                    ).observe(requireActivity) { generatingPaymentLinkforApplication ->
                        generatingPaymentLinkforApplication?.let { response ->
                            if (response.statusCode == 201) {
                                CommonUtils.toast(
                                    requireActivity,
                                    response.message ?: "Link Generated Created Successfully"
                                )
                                currentPage=1
                                leadPaymentList.clear()
                                adapterFragmentPayments.notifyDataSetChanged()
                                fetchDataFromApi(currentPage)
                                this@FragmentPayments.payment_type_identifier =""
                                this@FragmentPayments.payment_gateway_identifier =""
                                bottomSheetDialog.dismiss()
                            } else if (response.statusCode == 409) {
                                CommonUtils.toast(requireActivity, response.message ?: "Exists")
                                this@FragmentPayments.payment_type_identifier =""
                                this@FragmentPayments.payment_gateway_identifier =""
                                bottomSheetDialog.dismiss()
                            } else {
                                CommonUtils.toast(requireActivity, response.message ?: "Notes Failed")
                            }
                        }

                    }
                }
            }

        }
    private fun calltheApiPay(
        context: FragmentActivity,
        identifier1: String,
        binding: ItempaymentotherBinding,
        value: String,

        ) {
        context.let { fragmentActivity ->
            CommonUtils.accessToken?.let { accessToken ->
                ViewModalClass().getApplicationPayLiveData(
                    fragmentActivity,
                    AppConstants.fiClientNumber,
                    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer $accessToken",
                    identifier1
                ).observe(fragmentActivity) { response ->
                    if (response != null && response.success && response.data != null) {
                        val data = response.data

                        if(value.equals("razorpay")){
                            Checkout.preload(context)
                            val co = Checkout()

                            co.setKeyID(data!!.gateway_info.content.public_key)
                            initPayment(context, data)

                        }
                        if(value.equals("stripe")){
                            startPayment(fragmentActivity,data!!.gateway_info.content.public_key,data!!.gateway_info.order_info.id)
                        }

                    } else {
                        val errorMessage = response?.message ?: "Failed to fetch data"
                        CommonUtils.toast(fragmentActivity, errorMessage)
                    }
                }
            } ?: run {
                CommonUtils.toast(fragmentActivity, "Access token is missing")
            }
        }
    }

    private fun startPayment(fragmentActivity: FragmentActivity,pK: String, sK: String) {
        PaymentConfiguration.init(fragmentActivity, pK)
        secretKey = sK

        if (secretKey != null) {
            paymentSheet.presentWithPaymentIntent( secretKey)
            CommonUtils.dismissProgress()
        } else {
            CommonUtils.dismissProgress()
            CommonUtils.toast(fragmentActivity , "Payment key not available")
        }
    }


    private fun initPayment(context: FragmentActivity?, data: Data?) {
        val activity: FragmentActivity = context!!
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", context.getString(R.string.app_name))

            options.put("image", context.getString(R.string.payment_icon))
            options.put("theme.color", data!!.gateway_info.theme);
            options.put("currency", data.gateway_info.payment_info.currency);
            options.put("order_id", data.gateway_info.order_info.id);
            options.put(
                "amount",
                data.gateway_info.payment_info.amount
            )

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", data.user_info.email)
            prefill.put("contact", "${data.user_info.country_code}${data.user_info.mobile}")

            options.put("prefill", prefill)
            co!!.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                leadPaymentList.clear()
                adapterFragmentPayments.notifyDataSetChanged()
                currentPage = 1
                fetchDataFromApi(currentPage)
                CommonUtils.toast(context, "Payment Success")
            }

            is PaymentSheetResult.Canceled -> {
                CommonUtils.toast(context, "Payment Cancelled")
            }

            is PaymentSheetResult.Failed -> {
                val errorMessage = (paymentSheetResult as? PaymentSheetResult.Failed)?.error?.message ?: "Unknown error occurred"
                CommonUtils.toast(context, "Payment Failed: $errorMessage")
            }
        }
    }


}