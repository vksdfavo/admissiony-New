package com.student.Compass_Abroad.uniteli.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.AmbassadorChatLayoutBinding
import com.student.Compass_Abroad.modal.findAmbassadorModal.RecordsInfo

class FindAmbassadorChatAdapter(
    var requireActivity: FragmentActivity,
    var arrayList1: MutableList<RecordsInfo>,
    var selector: OnChatClick
) : RecyclerView.Adapter<FindAmbassadorChatAdapter.ViewHolder>() {

    interface OnChatClick {

        fun onClick(recordInfo: RecordsInfo)
        fun onClickBio(recordInfo: RecordsInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AmbassadorChatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = arrayList1[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return arrayList1.size

    }

    inner class ViewHolder(private val binding: AmbassadorChatLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RecordsInfo) {

            binding.rrBio.setOnClickListener {

                selector?.onClickBio(data)
            }

            binding.rrChat.setOnClickListener {
                selector?.onClick(data)
            }
            val firstName = data.userInfo.first_name?.takeIf { it.isNotBlank() }
            val lastName = data.userInfo.last_name?.takeIf { it.isNotBlank() }

            val fullName = listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { "" }

            binding.name.text = fullName

            binding.institutionData.text = data.institution_data?.name ?:"----"
            binding.programData.text = data.program_data?.name ?: "----"
            Glide.with(requireActivity)
                .load(data.userInfo.profile_picture_url)
                .placeholder(R.drawable.test_image)
                .error(R.drawable.test_image)
                .into(binding.profileImage)


            val iconUrl = data.institution_data?.country_data?.icon_url
            val addressParts = listOf(data.country_of_origin?.name, data.state_of_origin?.name, data.city_of_origin?.name).filterNotNull().filter { it.isNotBlank() }

            val address = addressParts.joinToString(" / ")
            binding.address.text = if (address.isNotBlank()) address else "----"

            Glide.with(requireActivity)
                .load(iconUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.flag)

        }
    }
}
