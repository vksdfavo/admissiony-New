package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils

import com.student.Compass_Abroad.databinding.FragmentSubProgramDetailsBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.applicationProgramDetails.ApplicationProgramResponse
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramChecklistInfo

import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass


class SubProgramDetails : BaseFragment() {


    private lateinit var binding: FragmentSubProgramDetailsBinding

    var value: Int = 0

    private val applicationProgram: ArrayList<ProgramInfo> = ArrayList()
    private val applicationProgramCheckListInfo: ArrayList<ProgramChecklistInfo> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSubProgramDetailsBinding.inflate(inflater, container, false)


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
                    setData(applicationProgram, applicationProgramCheckListInfo)

                } else {
                    CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }

    private fun setData(
        applicationProgram: java.util.ArrayList<ProgramInfo>,
        applicationProgramCheckListInfo: java.util.ArrayList<ProgramChecklistInfo>
    ) {

        if (applicationProgram != null) {
            val name = applicationProgram.get(0).program.name ?: ""
            val duration = applicationProgram.get(0).program.duration ?: 0
            val campus = applicationProgram.get(0).campus?.name ?: ""
            val tuitionFee = applicationProgram.get(0).tuition_fee ?: ""
            val applicationFee = applicationProgram.get(0).application_fee ?: ""

            val currency =
                applicationProgram.get(0).program.institution?.country?.currency_code ?: ""
            val currencySymbol =
                applicationProgram.get(0).program.institution?.country?.currency_symbol ?: ""

            binding.tvApdProgramName.text = if (name.isNotEmpty()) name else "-----"
            binding.tvDuration.text = if (duration > 0) "Duration: $duration months" else "-----"
            binding.tvCampus.text = "Campus Name: $campus"
            binding.tvTutionFee.text = "Tuition Fee: $tuitionFee $currencySymbol ($currency)"
            binding.tvApplicationFee.text =
                "Application Fee: $applicationFee $currencySymbol ($currency)"

            binding.tvApplied.setBackgroundResource(R.drawable.ic_preference_back)
             value =  App.singleton!!.position!!.toInt()
            var data1 = value+1
            binding.tvApplied.text = "PREFERENCE #${data1}"
            binding.tvApplied.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.btnPurple
                )
            )

            binding.clickViewDetail.visibility = View.GONE
            binding.view.visibility = View.GONE
        } else {
            Toast.makeText(requireActivity(), "No Data Found", Toast.LENGTH_SHORT).show()
        }


    }
}