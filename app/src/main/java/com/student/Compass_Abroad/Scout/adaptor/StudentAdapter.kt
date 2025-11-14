package com.student.Compass_Abroad.Scout.adaptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.navigation.R
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.ItemStudentBinding
import com.student.Compass_Abroad.databinding.ItemStudentsBinding
import com.student.Compass_Abroad.fragments.home.ApplicationActiveFragment
import com.student.Compass_Abroad.modal.getLeads.Record

class StudentAdapter(var context:Context,private var leadList: ArrayList<Record> = ArrayList()) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
       // holder.bind(leadList[position])
    }

    override fun getItemCount(): Int = 10

    // You can directly modify leadList and call notifyDataSetChanged() from your Fragment or Activity
    inner class StudentViewHolder(private val binding: ItemStudentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            // Handle null safety with Elvis operator and safe calls
            /*val fullName = "${item.first_name.orEmpty()} ${item.last_name.orEmpty()}"
            binding.tvName.text = if (fullName.isNotBlank()) fullName else "N/A"
            binding.tvEmail.text = item.email ?: "N/A"
            binding.number.text = item.mobile ?: "N/A"
            val country = item.country ?: "N/A"
            val state = item.state ?: "N/A"
            val city = item.city ?: "N/A"

            binding.loc.text = "$country, $state, $city"
            binding.tvItemStudentsStudentId.text = "Student ID: ${item.id ?: "N/A"}"

            val countryCode = item.country_code ?: ""
            val mobile = item.mobile ?: ""
            val fullNumber = "$countryCode$mobile"

*/

          /*  binding.fabFdStuCoordinatorCall.setOnClickListener {
                if (fullNumber.trim().isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL) // or ACTION_CALL (requires permission)
                    intent.data = Uri.parse("tel:+$fullNumber")
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
                }
            }*/
            binding.root.setOnClickListener {
                ApplicationActiveFragment.data=item.identifier
                ApplicationActiveFragment.data2=item
                AppConstants.profileStatus="1"
                Navigation.findNavController(binding.root).navigate(com.student.Compass_Abroad.R.id.applicationActiveFragment3)
            }


        }
    }
}
