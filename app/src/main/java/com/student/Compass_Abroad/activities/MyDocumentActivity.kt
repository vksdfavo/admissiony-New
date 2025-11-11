@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.adaptor.AdapterDestinationCountrySelector
import com.student.Compass_Abroad.adaptor.AdapterModuleType
import com.student.Compass_Abroad.adaptor.AdapterMyDocuments
import com.student.Compass_Abroad.databinding.ActivityMyDocumentBinding
import com.student.Compass_Abroad.modal.documentType.Data
import com.student.Compass_Abroad.modal.documentType.DocumentTypeModal
import com.student.Compass_Abroad.modal.documentType.RecordsInfo
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange
import kotlin.let

class MyDocumentActivity : AppCompatActivity() {
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    private val moduleTypeListCountry: MutableList<RecordsInfo> = mutableListOf()

    private lateinit var binding: ActivityMyDocumentBinding
    private lateinit var adaptermyDocument: AdapterMyDocuments

    private val recordInfoList: MutableList<com.student.Compass_Abroad.modal.getLeadsDocuments.Data> = mutableListOf()

    private val viewModel: ViewModalClass by lazy { ViewModalClass() }

    var identifier = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = getColor(android.R.color.white)

        window.navigationBarColor = getColor(android.R.color.white)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        identifier = (intent.getSerializableExtra("identifier") as? String).toString()
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
        setRecyclerView()

        binding.backBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        binding.moduleType.setOnClickListener {

            getModuleType(this, binding.moduleType)

        }

        fetchDataFromApi()

    }

    private fun getModuleType(requireActivity: FragmentActivity, moduleTypeTextView: TextView) {
        LoginViewModal().getDocumentType(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            "lead"
        ).observe(requireActivity) { documentType: DocumentTypeModal? ->
            documentType?.let { doc ->
                if (doc.statusCode == 200) {
                    doc.data?.recordsInfo?.let { recordsList ->
                        moduleTypeListCountry.clear()
                        moduleTypeListCountry.addAll(recordsList)
                        setModuleTypeList(moduleTypeTextView)
                    } ?: run {
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve destinationCountry. Please try again."
                        )
                    }
                } else {
                    CommonUtils.toast(requireActivity, doc.message ?: "Failed")
                }
            }
        }
    }


    private fun setModuleTypeList(moduleTypeTextView: TextView) {
        moduleTypeTextView.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val layout: View =
                LayoutInflater.from(this).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.findViewById<TextView>(R.id.etSelect).setHint("Search Module type")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = moduleTypeTextView.width

            val locationOnScreen = IntArray(2)
            moduleTypeTextView.getLocationOnScreen(locationOnScreen)
            val xLocationOfView = locationOnScreen[0]
            val yLocationOfView = locationOnScreen[1] + moduleTypeTextView.height

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter =
                AdapterModuleType(this, moduleTypeListCountry, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                moduleTypeTextView.text = selectedCountry.label

                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(moduleTypeTextView)

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



    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }
    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFa.layoutManager = layoutManager

        adaptermyDocument = AdapterMyDocuments(this, recordInfoList)
        binding.rvFa.adapter = adaptermyDocument

    }

    private fun fetchDataFromApi() {
        binding.pbFadAs.visibility = View.VISIBLE

        viewModel.getLeadDocumentsResponseLiveData(
            this,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            identifier,
            1,
            10,
            "desc",
            "id"
        ).observe(this) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {

                    val recordInfoListResponse = it.data ?: emptyList()

                    recordInfoList.clear()

                    recordInfoList.addAll(recordInfoListResponse)

                    adaptermyDocument.notifyDataSetChanged()

                    if (recordInfoList.isEmpty()) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvFa.visibility = View.GONE
                    } else {
                        binding.llSaaNoData.visibility = View.GONE
                        binding.rvFa.visibility = View.VISIBLE
                    }
                } else {
                    CommonUtils.toast(this, it.message ?: "Failed")
                    binding.llSaaNoData.visibility = View.VISIBLE
                    binding.rvFa.visibility = View.GONE
                }
            } ?: run {
                binding.llSaaNoData.visibility = View.VISIBLE
                binding.rvFa.visibility = View.GONE
            }

            binding.pbFadAs.visibility = View.GONE
        }
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
}