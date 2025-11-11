package com.student.Compass_Abroad.adaptor.dashBoardAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ItemTopPrefCountriesBinding
import com.student.Compass_Abroad.modal.getDestinationCountryList.Data
import java.util.Locale

class TopPreferCountryAdapter(var requireActivity: FragmentActivity?, var  arrayList1: ArrayList<Data>,var selecter: select) : RecyclerView.Adapter<TopPreferCountryAdapter.ViewHolder>() {


    interface select{
      fun  onClick()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use ViewBinding to inflate the layout
        val binding = ItemTopPrefCountriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)


    }

    // bind view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = arrayList1.getOrNull(position)

        holder.itemView.setOnClickListener {
            val valueString = currentItem!!.value.toString() // Ensure it's a string
            val labelString = currentItem.label.toString() // Ensure it's a string

            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)

            clearAllSelectedValues()
            saveSelectedToSharedPreferences(AppConstants.CountryList, valueString, labelString)

            selecter.onClick()



        }



        if (currentItem != null) {
            // Set the college name
            holder.binding.tvApdCollegeName.text = currentItem.label ?: "Unknown College"

            // Check if value is not null and not empty
            val index = currentItem.value

            if (index != null) {
                // Format the index to three digits with leading zeros
                val formattedIndex = String.format(Locale.getDefault(), "%03d", index.toInt())

                // Build the image URL
                val imageUrl = "https://static.frm.li/images/flags/$formattedIndex.png"
                val imageUri = Uri.parse(imageUrl)

                // Use Glide to load the image
                Glide.with(holder.itemView.context)
                    .load(imageUri)
                    .placeholder(R.drawable.zls) // Optional: placeholder while loading
                    .into(holder.binding.ivFlag)
            } else {
                // Handle the case where index is null or empty
                holder.binding.ivFlag.setImageResource(R.drawable.zls) // Use a placeholder image
            }
        } else {
            // Handle the case where currentItem is null
            holder.binding.tvApdCollegeName.text = "No Data"
            holder.binding.ivFlag.setImageResource(R.drawable.zls) // Use a placeholder image
        }


    }

    override fun getItemCount(): Int {
        return arrayList1.size
    }

    // view holder
    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemTopPrefCountriesBinding
    ) : RecyclerView.ViewHolder(
            binding.getRoot()
    )

    private fun saveSelectedToSharedPreferences(
        keyPrefix: String,
        ids: String,
        labels:String
    ) {
        val sharedPrefs = SharedPrefs(requireActivity!!)
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }


    private fun clearAllSelectedValues() {
        // Call this method for each key prefix you use
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }
    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {

        val sharedPrefs = SharedPrefs(requireActivity!!)
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }
}
