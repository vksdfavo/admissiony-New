package com.student.Compass_Abroad.Scout.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.adaptor.BankDetailsAdapter
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.Info
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.RecordsInfo
import com.student.Compass_Abroad.Scout.modalClass.getBankDetails.Field
import com.student.Compass_Abroad.Scout.retrofitScout.ViewModalScout
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityBankDetailsBinding
import com.student.Compass_Abroad.databinding.BottomSheetSetIsPrimaryBinding
import com.student.Compass_Abroad.fragments.home.FragmentNotes.Companion.data
import com.student.bt_global.Utils.NeTWorkChange

@Suppress("DEPRECATION")
class BankDetails : AppCompatActivity(), BankDetailsAdapter.SetOnClickListener {

    private lateinit var binding: ActivityBankDetailsBinding
    private var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    private var bankDetailsAdapter: BankDetailsAdapter? = null
    private val bankDetailsList: MutableList<RecordsInfo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankDetailsBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        setContentView(binding.root)
        setRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        binding.addBankAccount.setOnClickListener {
            startActivity(Intent(this, AddBankAccountActivity::class.java))
        }

        fetchDataFromApi()

        binding.fabBiBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        bankDetailsAdapter = BankDetailsAdapter(this, bankDetailsList, bankDetailsList, this)
        binding.recyclerView.adapter = bankDetailsAdapter
    }

    private fun fetchDataFromApi() {
        ViewModalScout().getBankAccountLiveData(
            this,
            "Bearer ${CommonUtils.accessToken}",
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            sharedPre?.getString(AppConstants.User_IDENTIFIER, "").toString()
        ).observe(this, Observer { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val newBankDetails = it.data?.recordsInfo ?: emptyList()
                    bankDetailsList.clear()
                    bankDetailsList.addAll(newBankDetails)
                    bankDetailsAdapter?.notifyDataSetChanged()

                    Log.d("fetchDataFromApi", newBankDetails.size.toString())

                    if (bankDetailsList.isEmpty()) {
                        binding.llFpApNoData.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.llFpApNoData.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }

                } else {
                    CommonUtils.toast(this, it.message ?: "Failed")
                    binding.llFpApNoData.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            } ?: run {
                binding.llFpApNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        })

    }

    override fun onResume() {
        super.onResume()

        fetchDataFromApi()
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(neTWorkChange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(neTWorkChange)
        super.onStop()
    }

    override fun listener(record: RecordsInfo) {
        val inflater = LayoutInflater.from(this)
        val binding = BottomSheetSetIsPrimaryBinding.inflate(inflater, null, false)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheet2)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
        binding.select.setOnClickListener {
            ViewModalScout().makePrimaryAccountFormListLiveData(
                this,
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                record.identifier
            ).observe(this, Observer { response ->
                response?.let {
                    if (it.statusCode == 200 && it.success) {
                        bankDetailsAdapter?.notifyDataSetChanged()
                        bottomSheetDialog.dismiss()

                        setRecyclerView()
                        fetchDataFromApi()

                    } else {
                        CommonUtils.toast(this, it.message ?: "Failed")
                    }
                }
            })
        }
        binding.cancelAction.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }


}
