package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemProgramRecomBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.ProgramTags.RecordsInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass

class AdapterProgramsShortListedProgram (
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<Record>,
    private var selectListener: select,
    private var lifecycleOwner: LifecycleOwner

) : RecyclerView.Adapter<AdapterProgramsShortListedProgram.MyViewHolder>() {
    var context: Context? = null
    var contentKey = ""
    private var programTagAdapter: ProgramTagAdapter? = null
    private val applicationList: MutableList<RecordsInfo> = mutableListOf()

    interface select {
        fun onCLick(record: Record);
        fun likeClick(record: Record, position: Int);
        fun disLikeCLick(record: Record, position: Int);
        fun openTagCLick(record: Record, position: Int);
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding =
            ItemProgramRecomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=arrayList1[position]

       holder.bind(currentItem)

        if (currentItem.is_shortlisted == 0) {
            holder.binding.ibHeart.setVisibility(View.VISIBLE)
            holder.binding.ibHeart2.setVisibility(View.GONE)
        } else {
            holder.binding.ibHeart.setVisibility(View.GONE)
            holder.binding.ibHeart2.setVisibility(View.VISIBLE)
        }


        if (App.singleton?.assignStaffFav =="1")
        {
            holder.binding.ibHeart.setVisibility(View.GONE)
            holder.binding.ibHeart2.setVisibility(View.GONE)
        }else if (App.singleton?.assignStaffFav =="0"){
            holder.binding.ibHeart.setVisibility(View.VISIBLE)
            holder.binding.ibHeart2.setVisibility(View.VISIBLE)
        }


        holder.binding.ibHeart.setOnClickListener {
            holder.binding.ibHeart.setVisibility(View.GONE)
            holder.binding.ibHeart2.setVisibility(View.VISIBLE)
            selectListener.likeClick(currentItem, position)

            arrayList1[position].is_shortlisted = 1;

        }

        holder.binding.ibHeart2.setOnClickListener {
            holder.binding.ibHeart2.setVisibility(View.GONE)
            holder.binding.ibHeart.setVisibility(View.VISIBLE)

            selectListener.disLikeCLick(currentItem, position)

            arrayList1[position].is_shortlisted = 0;
        }

        holder.itemView.setOnClickListener {

            selectListener.onCLick(currentItem)

        }

        holder.binding.menuApplications.setOnClickListener {

            selectListener.openTagCLick(currentItem, position)

        }

        if (!currentItem.program.tags.isNullOrEmpty()) {

            holder.binding.recyclerLay.visibility = View.VISIBLE
            val tagsAdapter = ProgramTagAdapter(currentItem.program.tags)
            holder.binding.recyclerTags.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            holder.binding.recyclerTags.adapter = tagsAdapter
        } else {
            holder.binding.recyclerLay.visibility = View.GONE
        }
        //showProgramTags(holder.binding, currentItem)

    }


   /* private fun showProgramTags(binding: ItemProgramRecomBinding, currentItem: Record) {
        val layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerTags.layoutManager = layoutManager

        // Fetch data first, then set up the adapter
        fetchDataFromApi(binding)

    }*/

    /*private fun fetchDataFromApi(binding: ItemProgramRecomBinding) {
        ViewModalClass().getPrograTagsList(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            "program"
        ).observe(lifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.recordsInfo ?: emptyList()

                    Log.d("fetchDataFromApi", "programResponse size: ${programResponse.size}")

                    // Update visibility
                    if (binding.view != null && binding.recyclerTags != null && binding.menuApplications != null) {
                        if (programResponse.isEmpty()) {
                            binding.view.visibility = View.GONE
                            binding.recyclerLay.visibility = View.GONE
                        } else {
                            binding.view.visibility = View.VISIBLE
                            binding.recyclerTags.visibility = View.VISIBLE
                        }
                    }

                    // Update RecyclerView
                    applicationList.clear()
                    applicationList.addAll(programResponse)

                    // Set up adapter after data is fetched
                    programTagAdapter = ProgramTagAdapter(applicationList)
                    binding.recyclerTags.adapter = programTagAdapter
                    programTagAdapter?.notifyDataSetChanged()

                    Log.d("AdapterCheck", "Data size: ${applicationList.size}, First Item: ${applicationList.getOrNull(0)}")
                } else {
                    Log.e("fetchDataFromApi", "Error fetching data: ${response.message}")
                }
            }
        }
    }*/


    class MyViewHolder(
        var binding: ItemProgramRecomBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(record: Record) {

            binding.apply {
                tvApdProgramName.text = record.program?.name ?: ""
                tvIADetailIntake.text = record.program?.intakes?.getOrNull(0)?.intake_name ?: ""
                val currencyCode = record.program?.institution?.country?.currency_code ?: ""
                val durationType=record.program.duration_type?:""
                val isLanguageProgram = record.program?.additional_items?.duration_range != null
                val isHigherEducation = record.program?.duration != null

                val tuitionFee = if (isLanguageProgram) {
                    record.program?.additional_items?.tuition_fee ?: ""
                } else {
                    record.tuition_fee?.toString() ?: ""
                }

                civItemAaStatus.text = "$tuitionFee $currencyCode"
//                tv6.text = "Duration"
                val duration = if (isHigherEducation) {
                    "${record.program?.duration ?: ""} $durationType"
                } else {
                    record.program?.additional_items?.duration_range ?: ""
                }
                ivItemProgramRecomDuration.text = duration

                // Set university name and country name
                val universityName = record.program.institution.name
                val countryName = record.program.institution.country.name
                ivItemProgramRecomCountry.text = countryName
                tvIADetailIntake.text = universityName


                Glide.with(binding.root)
                    .load(record.program.institution.logo)
                    .into(binding.ivItemProgramRecom)

                if (!record.program.tags.isNullOrEmpty()) {

                    binding.recyclerTags.visibility = View.VISIBLE
                    val tagsAdapter = ProgramTagAdapter(record.program.tags)
                    binding.recyclerTags.layoutManager =
                        LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                    binding.recyclerTags.adapter = tagsAdapter
                } else {
                    binding.recyclerTags.visibility = View.GONE
                }
            }



        }
    }




    override fun getItemCount(): Int {
        return arrayList1.size
    }
}