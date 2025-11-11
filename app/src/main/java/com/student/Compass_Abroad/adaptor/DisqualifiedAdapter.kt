package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.firmliagent.modal.getLeadModel.Record
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemleadBinding
import org.json.JSONArray

class DisqualifiedAdapter(private val context: Context, private val disqualifiedRecords: List<Record>) :
    RecyclerView.Adapter<DisqualifiedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemleadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: DisqualifiedAdapter.MyViewHolder, position: Int) {
        val record = disqualifiedRecords[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return disqualifiedRecords.size
    }

    inner class MyViewHolder(private val binding: ItemleadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record) {
            val firstName = record.first_name ?: ""
            val lastName = record.last_name ?: ""
            val name = "${firstName} ${lastName}"
            val birthday = if (record.birthday != null) {
                CommonUtils.convertDate(record.birthday)
            } else {
                "N/A"
            }

// If convertDate can return null, ensure to handle that case too
            val displayBirthday = birthday ?: "N/A"
            val id = record.id?.toString() ?: "N/A"
            val mobile = record.mobile ?: "N/A"
            val email = record.email ?: "N/A"
            val country = record.country ?: "N/A"
            val studyLevel = record.study_level ?: "N/A"
            val testScore = record.testscore ?: "N/A"
            var disciplineList: List<String>? = null
            try {
                val jsonArray = JSONArray(record.discipline)
                disciplineList = List(jsonArray.length()) { i -> jsonArray.getString(i) }
            } catch (e: Exception) {
                e.printStackTrace()
                disciplineList = emptyList()
            }

// Join the elements of the list into a single string
            val discipline = if (disciplineList!!.isNotEmpty()) {
                disciplineList!!.joinToString(", ")  // Join elements with a comma and a space
            } else {
                "N/A"
            }
            val destinationCountry = record.destination_country ?: "N/A"

// Bind your data to the views here
            binding.tvName.text = name
            binding.tvMyProfileDob.text = displayBirthday
            binding.tvItemStudentsStudentId.text = "Student Id:${id}"
            binding.number.text = mobile
            binding.email.text = email
            binding.location.text = country
            binding.tvQualification.text = "Qualification:${studyLevel}"
            binding.tvTestScore.text = "English Test Score:${testScore}"
            binding.tvAreaOfInterest.text = "Area Of Interest:${discipline}"
            binding.tvWantToStudyIn.text ="Country You Want to Study In :${destinationCountry}"

// Example: Set OnClickListener for chat button
            binding.tvCreateCounselling.setOnClickListener {
                // Handle chat button click here
            }
        }
    }
}