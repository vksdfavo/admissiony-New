package com.student.Compass_Abroad


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.context
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ItemInDemandIntuitionsBinding

class TopInDemandIntuitionsAdapter(private val destinationList: List<com.student.Compass_Abroad.modal.in_demandInstitution.Data>,
    private val onItemClick: ((com.student.Compass_Abroad.modal.in_demandInstitution.Data) -> Unit)? = null
) : RecyclerView.Adapter<TopInDemandIntuitionsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemInDemandIntuitionsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInDemandIntuitionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = destinationList[position]
        holder.binding.tvApdCollegeName.text = item.name
        holder.binding.countryName.text = item.country_name
        Glide.with(holder.itemView.context)
            .load(item.logo)
            .into(holder.binding.imgDestination)

        Glide.with(holder.itemView.context)
            .load(item.country_logo)
            .into(holder.binding.countryLogo)

        holder.itemView.setOnClickListener {

            val valueString = item!!.country_id.toString() // Ensure it's a string
            val labelString = item.country_name.toString() // Ensure it's a stri
            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)

            clearAllSelectedValues(holder.itemView.context)
            saveSelectedToSharedPreferences(AppConstants.CountryList, valueString, labelString, holder.itemView.context)
            onItemClick!!.invoke(item)

        }

    }

    private fun saveSelectedToSharedPreferences(
        keyPrefix: String,
        ids: String,
        labels: String,
        context: Context
    ) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }


    private fun clearAllSelectedValues(context: Context) {
        // Call this method for each key prefix you use
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }

    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }

    override fun getItemCount(): Int = destinationList.size
}
