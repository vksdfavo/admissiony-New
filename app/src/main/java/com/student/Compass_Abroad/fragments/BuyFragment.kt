package com.student.Compass_Abroad.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.razorpay.Checkout
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterModeOfPaymentVoucherSelector
import com.student.Compass_Abroad.adaptor.AdaptorVouchersRecyclerview
import com.student.Compass_Abroad.adaptor.AdaptorWebinarsRecyclerview
import com.student.Compass_Abroad.databinding.FragmentBuyBinding
import com.student.Compass_Abroad.databinding.FragmentClientEventsBinding
import com.student.Compass_Abroad.databinding.FragmentVouchersBinding
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.clientEventModel.Record
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject


class BuyFragment : BaseFragment() {

    private var binding: FragmentBuyBinding? = null
    private var adaptorVouchers: AdaptorVouchersRecyclerview? = null
    var arrayListVouchers = ArrayList<com.student.Compass_Abroad.modal.getVoucherModel.Record>()
    var isScrolling = false
    var currentVisibleItems = 0
    var totalItemsInAdapter: Int = 0
    var scrolledOutItems: Int = 0
    var dataPerPage = 20
    var presentPage: Int = 1
    var nextPage: Int = 0
    var secretKey: String = ""
    private var payment_gateway_identifier = ""
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var dialog: Dialog
    private val modeOfPaymentList: MutableList<com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo> =
        mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        binding = FragmentBuyBinding.inflate(getLayoutInflater(), container, false)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        onGetVouchers(requireActivity(), dataPerPage, presentPage)
        initPayment()
        arrayListVouchers.clear()


        // Inflate the layout for this fragment
        return binding!!.getRoot()
    }

    private fun onGetVouchers(
        requireActivity: FragmentActivity,
        presentPage: Int,
        dataPerPage: Int,
    ) {
        ViewModalClass().getVouchersModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, dataPerPage, presentPage
        ).observe(requireActivity()) { getVouchersModal: getVouchers? ->
            getVouchersModal?.let { nonNullForgetModal ->
                if (getVouchersModal.statusCode == 200) {
                    if (getVouchersModal.data?.records.isNullOrEmpty()) {
                        binding!!.rvVoucher.visibility = View.GONE
                        binding!!.noVoucherFound.visibility = View.VISIBLE
                    } else {
                        binding!!.rvVoucher.visibility = View.VISIBLE
                        binding!!.noVoucherFound.visibility = View.GONE
                        arrayListVouchers.addAll(getVouchersModal.data!!.records)
                        setVouchersRecyclerview(arrayListVouchers)
                    }
                } else {
                    val errorMessage = nonNullForgetModal.message ?: "Failed"

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMessage)
                    }
                }
            }
        }
    }


    private fun setVouchersRecyclerview(arrayList1: ArrayList<com.student.Compass_Abroad.modal.getVoucherModel.Record>) {
        adaptorVouchers =
            AdaptorVouchersRecyclerview(
                this.requireActivity(),
                arrayList1,
                arrayList1.size,
                object : AdaptorVouchersRecyclerview.Select {
                    @RequiresApi(Build.VERSION_CODES.P)
                    override fun select(
                        data: com.student.Compass_Abroad.modal.getVoucherModel.Record,
                        position1: Int,
                    ) {
                        BuyVoucherDialog(requireActivity(), data)
                    }

                })
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding!!.rvVoucher.layoutManager = layoutManager
        binding!!.rvVoucher.adapter = adaptorVouchers

        binding!!.rvVoucher.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    currentVisibleItems = layoutManager.childCount
                    totalItemsInAdapter = layoutManager.getItemCount()
                    val firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null)
                    val scrolledOutItems = firstVisibleItems.minOrNull() ?: 0
                    if (isScrolling && scrolledOutItems + currentVisibleItems == totalItemsInAdapter) {
                        isScrolling = false

                        //fetch data
                        if (presentPage < nextPage) {
                            presentPage += 1

                            Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                                override fun run() {
                                    Handler().removeCallbacks(this, null)
                                    onGetVouchers(requireActivity(), dataPerPage, presentPage)
                                }
                            }, 2000)
                        }
                    }
                }
            })

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun BuyVoucherDialog(
        requireActivity: FragmentActivity,
        data1: com.student.Compass_Abroad.modal.getVoucherModel.Record
    ) {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_buy_coupon)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        payment_gateway_identifier = ""
        val imgCourse = dialog.findViewById<ImageView>(R.id.imgCourse)
        val etQuantity = dialog.findViewById<EditText>(R.id.etQuantity)
        val tvTotal = dialog.findViewById<TextView>(R.id.tvTotal)
        val tvUnitPrice = dialog.findViewById<TextView>(R.id.tvUnitPrice)
        val tvAvailableVoucher = dialog.findViewById<TextView>(R.id.tvAvailableVoucher)
        val btnBuyNow = dialog.findViewById<Button>(R.id.btnBuyNow)
        val payment_mode = dialog.findViewById<TextView>(R.id.spinnerGateway)
        val tvCourseName = dialog.findViewById<TextView>(R.id.tvCourseName)
        val tvCourseDesc = dialog.findViewById<TextView>(R.id.tvCourseDesc)
        val tvUnitPriceStatic = dialog.findViewById<TextView>(R.id.tvUnitPriceStatic)

        val currency = data1.display_currency?.uppercase() ?: "USD"
        val unitPrice = data1.display_amount?.toDouble() ?: 200.0

        tvCourseName.text = data1.name
        tvCourseDesc.text = data1.description
        tvUnitPrice.text = "$currency ${String.format("%,.2f", unitPrice)}"
        tvUnitPriceStatic.text = "$currency ${String.format("%,.2f", unitPrice)}"
        tvAvailableVoucher.text = "Available Vouchers: ${data1.available_voucher ?: 0}"

        etQuantity.setText("1") // Set default quantity
        val initialQty = etQuantity.text.toString().toIntOrNull() ?: 0
        val initialTotal = initialQty * unitPrice
        tvTotal.text = "$currency ${String.format("%,.2f", initialTotal)}"

        getModeOfPaymentDropdown(requireActivity(), payment_mode)

        // Load image
        Glide.with(requireActivity)
            .load(data1.file_url)
            .placeholder(R.drawable.logo)
            .into(imgCourse)

        // TextWatcher to calculate total dynamically
        etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val qty = s?.toString()?.toIntOrNull() ?: 0
                val total = qty * unitPrice
                tvTotal.text = "$currency ${String.format("%,.2f", total)}"
            }


            override fun afterTextChanged(s: Editable?) {}
        })

        btnBuyNow.setOnClickListener {
            val quantity = etQuantity.text.toString().toIntOrNull()
            val selectedGateway = payment_gateway_identifier.toString()


            if (quantity == null || quantity <= 0) {
                Toast.makeText(requireActivity, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedGateway.isNullOrEmpty() || selectedGateway.equals("Select")) {
                Toast.makeText(
                    requireActivity,
                    "Please select a payment gateway",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val currency = data1!!.base_currency
            val price = data1!!.base_amount
            val qtyText = quantity
            val identifier = data1.identifier
            val gateway = selectedGateway


            // Proceed with API call
            generatingPaymentLinkVoucher(
                requireActivity,
                currency,
                price.toString(),
                qtyText.toString(),
                identifier,
                "FTY1734081664533I24VMFUF52",
                selectedGateway,
                dialog
            )


        }

        dialog.show()
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
            } else {
                // If not in a fragment, use activity view for positioning
                view?.getLocationOnScreen(locationOnScreen)
            }

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterModeOfPaymentVoucherSelector(requireActivity(), modeOfPaymentList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedModeOfPayment ->
                paymentSpinner?.text = selectedModeOfPayment.label
                payment_gateway_identifier = selectedModeOfPayment.value
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

    private fun generatingPaymentLinkVoucher(
        requireActivity: FragmentActivity,
        currency: String,
        price: String,
        quantity: String,
        moduleIdentifier: String,
        paymentTypeIdentifier: String,
        paymentGatewayIdentifier: String,
        dialog: Dialog
    ) {
        val accessToken = CommonUtils.accessToken
        val deviceIdentifier = App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""

        if (accessToken.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Access token is missing")
            return
        }

        ViewModalClass().genratingPaymentLinkVoucherLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            "Bearer $accessToken",
            "voucher",  // Ensure module = "voucher"
            moduleIdentifier,
            price,
            currency,
            quantity,
            paymentTypeIdentifier,
            paymentGatewayIdentifier
        ).observe(requireActivity) { response ->
            response?.let {
                when (it.statusCode) {
                    201 -> {

                        calltheApiPay(
                            requireActivity,
                            it.data!!.feePaymentInfo.identifier,
                            it.data!!.feePaymentInfo.payment_gateway_info.name
                        )


                    }

                    409 -> {
                        CommonUtils.toast(requireActivity, it.message ?: "Already exists")

                        dialog.dismiss()
                    }

                    else -> {
                        dialog.dismiss()
                        CommonUtils.toast(requireActivity, it.message ?: "Link creation failed")
                    }
                }
            }
        }
    }

    private fun calltheApiPay(
        context: FragmentActivity,
        identifier1: String,
        value: String

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

                        if (value.equals("Razorpay")) {
                            Checkout.preload(context)
                            val co = Checkout()

                            co.setKeyID(data!!.gateway_info.content.public_key)
                            initPayment(context, data)

                        }
                        if (value.equals("Stripe")) {
                            startPayment(
                                fragmentActivity,
                                data!!.gateway_info.content.public_key,
                                data!!.gateway_info.order_info.id
                            )
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

    private fun initPayment() {
        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            paymentSheetResult?.let {
                onPaymentResult(
                    it, dialog
                )
            }
        }
    }

    private fun startPayment(fragmentActivity: FragmentActivity, pK: String, sK: String) {
        PaymentConfiguration.init(fragmentActivity, pK)
        secretKey = sK

        if (secretKey != null) {
            paymentSheet.presentWithPaymentIntent(secretKey)
            CommonUtils.dismissProgress()
        } else {
            CommonUtils.dismissProgress()
            CommonUtils.toast(fragmentActivity, "Payment key not available")
        }
    }


    private fun initPayment(
        context: FragmentActivity?,
        data: com.student.Compass_Abroad.modal.getPaymentApplicationPay.Data?
    ) {
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
            dialog.dismiss()

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    private fun onPaymentResult(
        paymentSheetResult: PaymentSheetResult,
        dialog: Dialog
    ) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                dialog.dismiss()
                CommonUtils.toast(context, "Payment Success")
            }

            is PaymentSheetResult.Canceled -> {
                dialog.dismiss()
                CommonUtils.toast(context, "Payment Cancelled")
            }

            is PaymentSheetResult.Failed -> {
                val errorMessage =
                    (paymentSheetResult as? PaymentSheetResult.Failed)?.error?.message
                        ?: "Unknown error occurred"
                CommonUtils.toast(context, "Payment Failed: $errorMessage")

            }
        }
    }

    private fun getModeOfPaymentDropdown(
        requireActivity: FragmentActivity,
        payment_mode: TextView,
    ) {

        ViewModalClass().getModeOFPaymentDropDownVoucherLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { getVoucherPaymentMode: getVoucherPaymentMode? ->
            getVoucherPaymentMode?.let { nonNullcategoriesResponse ->
                if (getVoucherPaymentMode.statusCode == 200) {
                    modeOfPaymentList.clear()
                    if (getVoucherPaymentMode.data != null) {
                        for (i in 0 until getVoucherPaymentMode?.data?.recordsInfo!!.size) {
                            modeOfPaymentList?.add(getVoucherPaymentMode.data!!.recordsInfo[i])
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            PaymentModeSpinner(payment_mode)
                        }
                    } else {
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve testScore. Please try again."
                        );
                    }


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getVoucherPaymentMode.message ?: " Failed"
                    )
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible = false
    }

}