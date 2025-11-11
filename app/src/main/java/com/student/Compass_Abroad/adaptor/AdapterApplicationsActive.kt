package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.ItemApplicationsActiveBinding
import com.student.Compass_Abroad.fragments.home.ApplicationDetail
import com.student.Compass_Abroad.fragments.home.FragApplicationAssignedStaff
import com.student.Compass_Abroad.fragments.home.FragApplicationTimeline
import com.student.Compass_Abroad.fragments.home.FragmentApplicationDocument
import com.student.Compass_Abroad.fragments.home.FragmentNotes
import com.student.Compass_Abroad.fragments.home.FragmentPayments
import com.student.Compass_Abroad.fragments.home.FragmentPrograms
import com.student.Compass_Abroad.fragments.home.FragmentReminders
import com.student.Compass_Abroad.fragments.home.FragmentUploadDocuments
import com.student.Compass_Abroad.modal.getApplicationResponse.Record
import androidx.navigation.findNavController
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity


class AdapterApplicationsActive(var activity: Context, var applicationList: MutableList<Record>) :
    RecyclerView.Adapter<AdapterApplicationsActive.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemApplicationsActiveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applicationList[position]

        holder.binding.apply {
            /*   *//*tvName.text = "${application.leadInfo.first_name ?: ""} ${application.leadInfo.last_name ?: ""}"
            tvItemStudentsStudentId.text = " Student Id: ${application.leadInfo.id ?: ""}"
            tvEmail.text = "${application.leadInfo.email ?: ""}"
            val dob = application.leadInfo.birthday
            if (dob.isNullOrEmpty()) {
                tvMyProfileDob.text = "N/A"
            } else {
                try {
                    tvMyProfileDob.text = CommonUtils.convertDate2(dob)
                } catch (e: ParseException) {
                    tvMyProfileDob.text = "Invalid Date"
                }
            }*/


            if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

                holder?.binding?.rrChat?.visibility=View.GONE

            }else{

                holder?.binding?.rrChat?.visibility=View.VISIBLE

            }


            holder.binding.rrChat.setOnClickListener { v: View ->
                if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                    App.singleton?.applicationIdentifierChat = application.identifier
                    App.singleton?.idetity = "applications"
                    v.findNavController().navigate(R.id.fragmentAgentChat2)
                    App.singleton?.chatStatus = "2"
                } else {

                    App.singleton?.applicationIdentifierChat = application.identifier
                    App.singleton?.idetity = "applications"
                    v.findNavController().navigate(R.id.fragmentAgentChat)
                    App.singleton?.chatStatus = "2"
                }

            }
            tvApdProgramName.text = application.latestInstitutionInfo.institution_data.name ?: ""
            val institutionData = application.latestInstitutionInfo.institution_data





            if (application.allProgramInfo.isNotEmpty()) {
                holder.binding.programName.text =
                    "" + application.allProgramInfo[0].program_data.name
            } else {
                holder.binding.programName.text = "No program available"
            }
//
//            if (application.allProgramInfo.isNotEmpty()) {
//                val programNames = application.allProgramInfo.map { it.program_data.name }.joinToString(", ")
//                holder.binding.programName.text = "Program name: $programNames"
//            } else {
//                holder.binding.programName.text = "No program available"
//            }


// Set the text to show campus and country
            val campus = institutionData.campus ?: ""
            val country = institutionData.country ?: ""

            nameCountry.text = "$campus, $country"
            val applicationFeeInfo = application.applicationFeeInfo.firstOrNull()


            val intakeName = application.intakeInfo?.intake_name ?: ":0"
            val intakeYear = application.intakeInfo?.intake_year ?: ""
            // Safely retrieve values, providing a default empty string if null
            val currency = application.latestInstitutionInfo.institution_data.currency ?: ""
            val currencySymbol =
                application.latestInstitutionInfo.institution_data.currency_symbol ?: ""
            val programFee =
                application.allProgramInfo.getOrNull(0)?.program_data?.application_fee ?: ""

// Construct the application fee string only if all necessary values are available
            var applicationFee: String? =
                if (currency.isNotBlank() && currencySymbol.isNotBlank() && programFee.isNotBlank()) {
                    "$currency $currencySymbol $programFee"
                } else {
                    null // Set to null if any part is missing
                }


            val feeText = applicationFee?.let { "$it" } ?: ""

            tvItemAaIntakeFee.text = "$intakeName"

            applicationFeeNew.text = "$feeText"



            appId.text = "App Id: ${application.id}"
            tvStatus.text = application.statusInfo.name

            /* if(application.leadInfo.mobile !=null){
                 holder.binding.number.text=application.leadInfo.mobile
             }else{
                 holder.binding.number.text="-----"
             }*/



            holder.binding.cdLayoutLR.setOnClickListener { v: View ->
                if (application.identifier != null) {
                    FragApplicationTimeline.data = application
                    ApplicationDetail.data = application
                    FragApplicationAssignedStaff.data = application
                    FragmentNotes.data = application
                    FragmentPrograms.data = application
                    FragmentReminders.data = application
                    FragmentApplicationDocument.data = application
                    FragmentUploadDocuments.data = application
                    FragmentPayments.data = application
                    App.singleton?.applicationIdentifierChat = application.identifier

                    sharedPre?.let {

                        if (it.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                            v.findNavController().navigate(R.id.applicationDetail2)

                        } else {

                            v.findNavController().navigate(R.id.applicationDetail)

                        }
                    }
                    App.singleton?.chatStatus = "2"
                    App.singleton!!.applicationFee = applicationFee
                } else {
                    Toast.makeText(activity, "Identifier is null", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }

    class ViewHolder(var binding: ItemApplicationsActiveBinding) :
        RecyclerView.ViewHolder(binding.root)
}
