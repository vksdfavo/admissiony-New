package com.student.Compass_Abroad

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ItemInDemandIntuitionsBinding
import com.student.Compass_Abroad.modal.in_demandInstitution.Data
import java.util.Locale

class TopInDemandIntuitionsAdapter(
    private val destinationList: List<Data>,
    private val onItemClick: ((Data) -> Unit)? = null
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

        holder.binding.apply {
            tvApdCollegeName.text = item.name
            countryName.text = item.country_name

            Glide.with(holder.itemView.context)
                .load(item.logo)
                .placeholder(R.drawable.circle_img)
                .error(R.drawable.circle_img)
                .into(imgDestination)
            val flagUrl = getCountryFlagUrl(item.country_name)

            Glide.with(holder.itemView.context)
                .load(flagUrl)
                .placeholder(R.drawable.circle_img)
                .error(R.drawable.circle_img)
                .into(countryLogo)
        }

        holder.itemView.setOnClickListener {

            val valueString = item.country_id.toString()
            val labelString = item.country_name

            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)

            clearAllSelectedValues(holder.itemView.context)
            saveSelectedToSharedPreferences(
                AppConstants.CountryList,
                valueString,
                labelString,
                holder.itemView.context
            )

            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = destinationList.size

    // ================= FLAG UTILITY =================

    private fun getCountryFlagUrl(countryName: String?): String {
        if (countryName.isNullOrEmpty()) return ""

        val countryCode = Locale.getISOCountries().firstOrNull { code ->
            Locale("", code).displayCountry.equals(countryName, ignoreCase = true)
        }

        return countryCode?.let {
            "https://flagcdn.com/w320/${it.lowercase()}.png"
        } ?: ""
    }

    // ================= SHARED PREFS =================

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
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList, context)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList, context)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList, context)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList, context)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList, context)
    }

    private fun clearSelectedValuesFromSharedPreferences(
        keyPrefix: String,
        context: Context
    ) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }
}
