package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.ChecklistAdapter

import com.student.Compass_Abroad.databinding.FragmentSubCheckListBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.applicationProgramDetails.ApplicationProgramResponse
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramChecklistInfo
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass


class SubCheckList : BaseFragment() {

    private lateinit var binding: FragmentSubCheckListBinding
    private lateinit var checklistAdapter:ChecklistAdapter

    private val applicationProgram: ArrayList<ProgramInfo> = ArrayList()
    private val applicationProgramCheckListInfo: ArrayList<ProgramChecklistInfo> = ArrayList()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSubCheckListBinding.inflate(inflater, container, false)


        setDataToView()

        return binding.root
    }


    private fun setDataToView() {

        ViewModalClass().getApplicationProgramResponseData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            App.singleton!!.countryId.toString(),
            App.singleton!!.institution_id.toString(),
            App.singleton!!.programId.toString(),
            App.singleton!!.campusId.toString()
        ).observe(requireActivity()) { clientEventModal: ApplicationProgramResponse? ->
            clientEventModal?.let { nonNullForgetModal ->
                if (clientEventModal.statusCode == 200) {
                    applicationProgram.add(nonNullForgetModal.data!!.programInfo)
                    applicationProgramCheckListInfo.addAll(nonNullForgetModal.data!!.programChecklistInfo)

                    checklistAdapter = ChecklistAdapter(applicationProgramCheckListInfo)
                    binding.rv.layoutManager = LinearLayoutManager(context)
                    binding.rv.adapter = checklistAdapter

                } else {
                    CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }





}