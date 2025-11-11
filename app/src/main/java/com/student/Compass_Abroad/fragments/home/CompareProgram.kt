package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramsCompareProgram
import com.student.Compass_Abroad.databinding.FragmentCompareProgramBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import java.util.ArrayList
import kotlin.random.Random


class CompareProgram : BaseFragment(), AdapterProgramsCompareProgram.select {

    var binding: FragmentCompareProgramBinding? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adaptorCompareProgram: AdapterProgramsCompareProgram
    var arrayList1=java.util.ArrayList<com.student.Compass_Abroad.modal.shorlistedProgram.Record>()
    var isScrolling = false
    var currentVisibleItems = 0
    var totalItemsInAdapter: kotlin.Int = 0
    var scrolledOutItems: kotlin.Int = 0
    var dataPerPage = 6
    var presentPage: kotlin.Int = 1
    var nextPage: kotlin.Int = 0
    var contentKey=""
    private val selectedRecords = ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>()

    companion object {
        var detail: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompareProgramBinding.inflate(inflater, container, false)

        selectedRecords.clear()
        setAllProgramRecyclerview(detail)

        binding!!.tvViewComparision.setOnClickListener { v: View ->
            if (selectedRecords.size >= 2) {
                Comparison.comparisonList = selectedRecords
                Navigation.findNavController(binding!!.getRoot()).navigate(R.id.comparison)
            } else {
                Toast.makeText(requireActivity(), "Select at least two programs for comparison", Toast.LENGTH_LONG).show()
            }


    }


        binding!!.backBtn.setOnClickListener{

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding!!.root
    }



    private fun setAllProgramRecyclerview(arrayList1: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>?) {

        //recycler view
        if (isAdded) {
            linearLayoutManager = LinearLayoutManager(requireActivity())
        }


        binding?.rvFpAp?.setLayoutManager(linearLayoutManager)
        val dividerItemDecoration =
            DividerItemDecoration(binding?.rvFpAp?.getContext(), DividerItemDecoration.VERTICAL)
        binding?.rvFpAp?.addItemDecoration(dividerItemDecoration)
        adaptorCompareProgram =
            AdapterProgramsCompareProgram(requireActivity(), arrayList1!!, this)
        binding?.rvFpAp?.setAdapter(adaptorCompareProgram)


    }

    override fun onCLick(record: com.student.Compass_Abroad.modal.AllProgramModel.Record) {

    }


    override fun likeClick(
        record: com.student.Compass_Abroad.modal.AllProgramModel.Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        //form data with email login code start
        val formData = JSONObject();

        formData.put("program_campus_identifier", record.identifier) //email or phone
        val data = formData.toString();
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret

        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
            Log.d("sholisted", contentKey)

        } else {

            println("Encryption failed.")

        }

        addToShortlist(requireActivity(), contentKey)
    }

    override fun disLikeCLick(
        record:com.student.Compass_Abroad.modal.AllProgramModel.Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        var publicKey = hexString
        var privateKey = AppConstants.privateKey
//form data with email login code start
        val formData = JSONObject();

        formData.put("program_campus_identifier", record.identifier) //email or phone
        val data = formData.toString();
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret

        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
            Log.d("sholisted", contentKey)

        } else {

            println("Encryption failed.")

        }

        addToShortlist(requireActivity(), contentKey)
    }

    override fun onCheckboxSelected(record: com.student.Compass_Abroad.modal.AllProgramModel.Record, isChecked: Boolean) {
        if (isChecked) {
            selectedRecords.add(record)
        } else {
            selectedRecords.remove(record)
        }
    }


    private fun addToShortlist(
        requireActivity: FragmentActivity,
        content: String

    ) {

        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, content
        ).observe(requireActivity) { allShorListModal: ShortListResponse? ->
            allShorListModal?.let { nonNullForgetModal ->
                if (allShorListModal.statusCode == 200) {


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        allShorListModal.message ?: " Failed"
                    )
                }
            }
        }


    }

    fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }

            .joinToString("")
    }
    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible=false
    }
}



