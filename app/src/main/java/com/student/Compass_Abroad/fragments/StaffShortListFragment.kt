package com.student.Compass_Abroad.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.CommonUtils.addOnPaginationListener
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramsShortListedProgram
import com.student.Compass_Abroad.adaptor.ProgramTagAdapter
import com.student.Compass_Abroad.databinding.FragmentStaffShortListBinding
import com.student.Compass_Abroad.databinding.ProgramTagsDialogBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.home.ProgramDetails
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class StaffShortListFragment : Fragment(), AdapterProgramsShortListedProgram.select {
    private lateinit var binding: FragmentStaffShortListBinding
    private var arrayList1 = ArrayList<Record>()
    private lateinit var adapterShortListedProgram: AdapterProgramsShortListedProgram
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var contentKey = ""
    private var dataPerPage = 6
    private var presentPage = 1
    private var nextPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStaffShortListBinding.inflate(inflater, container, false)

        setupRecyclerView()



        return binding!!.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFpAp?.layoutManager = linearLayoutManager
        adapterShortListedProgram = AdapterProgramsShortListedProgram(
            requireActivity(),
            arrayList1,
            this,
            viewLifecycleOwner
        )
        binding?.rvFpAp?.adapter = adapterShortListedProgram
        binding?.rvFpAp?.addOnPaginationListener(nextPage) { nextPage ->
            if (presentPage < nextPage) {
                presentPage++
                binding?.pbFpApPagination?.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    onGetAllShorlistedPrograms(requireActivity(), dataPerPage, presentPage)
                }, 2000)
            }
        }
    }

    private fun loadInitialData() {
        binding?.pbFpAp?.visibility = View.VISIBLE
        arrayList1.clear()
        onGetAllShorlistedPrograms(requireActivity(), dataPerPage, presentPage)
    }

    private fun onGetAllShorlistedPrograms(
        requireActivity: FragmentActivity,
        dataPerPage: Int,
        presentPage: Int
    ) {
        ViewModalClass().getshortlistedModalLiveDataStaff(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            presentPage,
            dataPerPage,""
        ).observe(viewLifecycleOwner) { shorlistedProgramModal: AllProgramModel? ->
            shorlistedProgramModal?.let { nonNullForgetModal ->
                if (shorlistedProgramModal.statusCode == 200) {
                    nextPage = shorlistedProgramModal.data?.metaInfo?.nextPage ?: 0
                    val newRecords = shorlistedProgramModal.data?.records ?: emptyList()
                    newRecords.forEach { record ->
                        if (!arrayList1.contains(record)) {
                            arrayList1.add(record)
                        }
                    }
                    setRecyclerViewVisibility()
                } else {
                    CommonUtils.toast(requireActivity, nonNullForgetModal.message ?: "Failed")
                }
                binding?.pbFpAp?.visibility = View.GONE
                binding?.pbFpApPagination?.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerViewVisibility() {
        if (arrayList1.isEmpty()) {
            binding?.llFpApNoData?.visibility = View.VISIBLE
        } else {
            binding?.llFpApNoData?.visibility = View.GONE
        }
        adapterShortListedProgram.notifyDataSetChanged()
    }

    override fun onCLick(record: Record) {
        ProgramDetails.details = record
        Navigation.findNavController(binding!!.root).navigate(R.id.programDetails)
    }

    override fun likeClick(
        record: Record,
        pos: Int
    ) {
        handleLikeOrDislike(record, pos)
    }

    override fun disLikeCLick(
        record: Record,
        pos: Int
    ) {
        handleLikeOrDislike(record, pos)
    }

    override fun openTagCLick(record: Record, position: Int) {

        val itemBinding = ProgramTagsDialogBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = 200.dpToPx()
        val margin = resources.getDimensionPixelSize(R.dimen.space5)
        layoutParams.horizontalMargin = 0f
        dialog.window!!.decorView.setPadding(margin, 0, margin, 0)
        dialog.window!!.attributes = layoutParams


        itemBinding.backBtn.setOnClickListener {
            dialog.dismiss()
        }
        val layoutManager =
            LinearLayoutManager(binding!!.root.context, LinearLayoutManager.HORIZONTAL, false)
        itemBinding.recyclerTags.layoutManager = layoutManager


        if (record.program.tags.isNotEmpty()) {

            itemBinding.recyclerLay.visibility = View.VISIBLE
            val tagsAdapter = ProgramTagAdapter(record.program.tags)
            itemBinding.recyclerTags.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.recyclerTags.adapter = tagsAdapter
        } else {

            itemBinding.recyclerLay.visibility = View.GONE
        }

        dialog.show()
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun handleLikeOrDislike(
        record: Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        val formData = JSONObject()
        formData.put("program_campus_identifier", record.identifier)

        CommonUtils.toast(requireActivity(),record.identifier)

        val data = formData.toString()
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            Log.d("shorlisted", contentKey)
            removeFromShortlist(pos)
        } else {
            Log.d("shorlisted", "Encryption failed.")
        }
    }

    private fun removeFromShortlist(pos: Int) {
        addToShortlist(requireActivity(), contentKey) {
            arrayList1.removeAt(pos)
            adapterShortListedProgram.notifyItemRemoved(pos)
            setRecyclerViewVisibility()
        }
    }

    private fun addToShortlist(
        requireActivity: FragmentActivity,
        content: String,
        onSuccess: () -> Unit
    ) {
        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, content
        ).observe(requireActivity) { allShorListModal: ShortListResponse? ->
            allShorListModal?.let { nonNullForgetModal ->
                if (allShorListModal.statusCode == 200) {
                    onSuccess()
                } else {
                    CommonUtils.toast(requireActivity, allShorListModal.message ?: "Failed")
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
        presentPage = 1
        loadInitialData()
        MainActivity.bottomNav!!.isVisible = false
    }

}