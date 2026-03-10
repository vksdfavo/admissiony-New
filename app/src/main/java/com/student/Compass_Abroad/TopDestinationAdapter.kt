package com.student.Compass_Abroad

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ShimmerTopDestinationBinding
import com.student.Compass_Abroad.databinding.TopDestinationLayoutBinding
import com.student.Compass_Abroad.modal.top_destinations.Data
import java.util.Locale

class TopDestinationAdapter(
    private var context: Context,
    private var destinationList: List<Data>,
    private val onItemClick: ((Data) -> Unit)? = null,
    private var isLoading: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SHIMMER = 0
    private val VIEW_TYPE_DATA = 1

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    class DataViewHolder(val binding: TopDestinationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ShimmerViewHolder(val binding: ShimmerTopDestinationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ShimmerTopDestinationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = TopDestinationLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            DataViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) 6 else destinationList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ShimmerViewHolder) {
            holder.binding.shimmerTopDestination.startShimmer()
            return
        }

        holder as DataViewHolder
        val item = destinationList[position]

        holder.binding.apply {
            countryName.text = item.country_name
            totalIns.text = "${item.total_institutions} institutions"

            // Institution Image
            Glide.with(root.context)
                .load(item.country_logo)
                .placeholder(R.drawable.circle_img)
                .error(R.drawable.circle_img)
                .into(imgDestination)

            // 🌍 AUTO FLAG FROM COUNTRY NAME
            val flagUrl = getCountryFlagUrl(item.country_name)

            Glide.with(root.context)
                .load(flagUrl)
                .placeholder(R.drawable.circle_img)
                .error(R.drawable.circle_img)
                .into(flag)
        }

        holder.binding.itemContainers.setOnClickListener {
            val valueString = item.country_id.toString()
            val labelString = item.country_name

            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)

            clearAllSelectedValues()
            saveSelectedToSharedPreferences(
                AppConstants.CountryList,
                valueString,
                labelString
            )
            onItemClick?.invoke(item)
        }
    }

    fun updateList(newList: List<Data>) {
        isLoading = false
        destinationList = newList
        notifyDataSetChanged()
    }

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
        labels: String
    ) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }

    private fun clearAllSelectedValues() {

        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)

    }

    private fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }
}
