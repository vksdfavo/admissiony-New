package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentSubAdditionalInfoBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.applicationProgramDetails.ApplicationProgramResponse
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramChecklistInfo
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass


class SubAdditionalInfo : BaseFragment() {

    private lateinit var binding:FragmentSubAdditionalInfoBinding

    private val applicationProgram: ArrayList<ProgramInfo> = ArrayList()
    private val applicationProgramCheckListInfo: ArrayList<ProgramChecklistInfo> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentSubAdditionalInfoBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment

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


                    applicationProgram?.let { programInfo ->
                        binding.textViewFullTimeValue.text = applicationProgram.get(0).program.is_fulltime ?: "N/A"
                        binding.textViewAttendanceValue.text = applicationProgram.get(0).program.attendance_on ?: "N/A"
                        binding.textViewStudyLevelValue.text = applicationProgram.get(0).program.studylevel?.name ?: "N/A"
                        binding.textViewDisciplineValue.text = applicationProgram.get(0).program.discipline?.name ?: "N/A"
                    } ?: run {
                        // Handle case where `data` is null
                        binding.textViewFullTimeValue.text = "N/A"
                        binding.textViewAttendanceValue.text = "N/A"
                        binding.textViewStudyLevelValue.text = "N/A"
                        binding.textViewDisciplineValue.text = "N/A"
                    }

                } else {
                    CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }

}