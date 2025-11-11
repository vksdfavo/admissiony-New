package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ItemAreaOfInterestLayoutBinding
import com.student.Compass_Abroad.modal.preferCountryList.Data

class AreaOfInterestAdaptor(

    private val context: Context,
    private val list: List<Data>,
    private val callback: Select,
    private val maxItemCount: Int

) : RecyclerView.Adapter<AreaOfInterestAdaptor.MyViewHolder>() {

    interface Select {

        fun selectItemFilter(data: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAreaOfInterestLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.countryName.text = currentItem.label

        Glide.with(context)
            .load(currentItem.icon_url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.binding.imageviewData1Circleimageview)


        holder.itemView.setOnClickListener {


            val valueList = arrayListOf(currentItem.value)
            val labelList = arrayListOf(currentItem.label)


            App.sharedPre!!.clearKey(AppConstants.PGWP_KEY)
            App.sharedPre!!.clearKey(AppConstants.ATTENDANCE_KEY)
            App.sharedPre!!.clearKey(AppConstants.PROGRAM_TYPE_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
            App.sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)
            clearAllSelectedValues()
            saveSelectedValuesToSharedPreferences(AppConstants.disciplineList, valueList, labelList)

            callback.selectItemFilter(currentItem)
        }


    }

    override fun getItemCount(): Int {
        return if (list.size > maxItemCount) maxItemCount else list.size
    }

    class MyViewHolder(val binding: ItemAreaOfInterestLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun saveSelectedValuesToSharedPreferences(
        keyPrefix: String,
        ids: List<String>,
        labels: List<String>,
    ) {
        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.putStringList("${keyPrefix}Id", ids)
        sharedPrefs.putStringList("${keyPrefix}Label", labels)
    }


    private fun clearAllSelectedValues() {
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }

    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {

        val sharedPrefs = SharedPrefs(context)
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }

}
