package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemScheduledLayoutBinding
import com.student.Compass_Abroad.databinding.ProgramTagLayoutBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Tags
import com.student.Compass_Abroad.modal.ProgramTags.ProgramTags
import com.student.Compass_Abroad.modal.ProgramTags.RecordsInfo
import com.student.Compass_Abroad.modal.counsellingModal.Record
import java.text.SimpleDateFormat
import java.util.*

class ProgramTagAdapter(
    var arrayList1: List<Tags>
) : RecyclerView.Adapter<ProgramTagAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProgramTagLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList1[position]


        holder.binding.tvTag.text = currentItem.name
        Log.d("fetchDataFromApi", currentItem.name)


    }

    override fun getItemCount(): Int = arrayList1.size

    class ViewHolder(var binding: ProgramTagLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}