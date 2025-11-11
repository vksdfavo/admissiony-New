package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ReminderItemLayoutBinding
import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.modal.getApplicationRemider.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RemindersAdapter(var context: FragmentActivity?,var  applicationReminderList: MutableList<Record>,var selector: Select):RecyclerView.Adapter<RemindersAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ReminderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    interface Select{
        fun onClick()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = applicationReminderList[position]
        holder.bind(data, context,selector)
    }

    override fun getItemCount(): Int {
        return applicationReminderList.size
    }

    class MyViewHolder(var binding: ReminderItemLayoutBinding) : RecyclerView.ViewHolder(

        binding.getRoot()
    ) {
        fun bind(data: Record, context: FragmentActivity?, selector: Select) {
            //  Toast.makeText(context,data.created_at,Toast.LENGTH_LONG).show()


            val firstName = data.created_by_info?.first_name ?: "Name not available"
            val lastName = data.created_by_info?.last_name ?: ""
            val createdDate = if (data.created_at != null) CommonUtils.convertDate3(
                data.created_at,
                "dd-MMM-yyyy hh:mm:ss a"
            ) else "NA"
            val title = data.title ?: "title Not Available"
            val status = data.status ?: "Status Not Available"
            val identifier = data.identifier ?: "Id Not Available"
            val destination = data.created_by_role_info.name ?: "destination Not Available"

            binding.tvIdentifier.setText(identifier)


            val publicKey = data.remarkInfo.content_key
            val privateKey = AppConstants.privateKey
            val appSecret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"

            if (data?.status == "completed") {
                binding.tvStatus.text = data.status
                val backgroundDrawable = GradientDrawable()
                backgroundDrawable.cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    context!!.resources.displayMetrics
                )
                backgroundDrawable.setColor(Color.parseColor("#F4FCF3"))
                binding.cd.setBackgroundDrawable(backgroundDrawable)
            } else if (data?.status == "assigned") {
                val backgroundDrawable = GradientDrawable()
                backgroundDrawable.cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    context!!.resources.displayMetrics
                )
                backgroundDrawable.setColor(Color.parseColor("#DDCBCA"))
                binding.cd.setBackgroundDrawable(backgroundDrawable)
                binding.tvStatus.text = data.status
            } else if (data?.status == "working") {
                val backgroundDrawable = GradientDrawable()
                backgroundDrawable.cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    context!!.resources.displayMetrics
                )
                backgroundDrawable.setColor(Color.parseColor("#6FA8DC"))
                binding.cd.setBackgroundDrawable(backgroundDrawable)
                binding.tvStatus.text = data.status

            } else if (data?.status == "cancelled") {
                val backgroundDrawable = GradientDrawable()
                backgroundDrawable.cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    context!!.resources.displayMetrics
                )
                backgroundDrawable.setColor(Color.parseColor("#D77B74"))
                binding.cd.setBackgroundDrawable(backgroundDrawable)
                binding.tvStatus.text = data.status
            }

            if(data.status.equals("assigned")){
                binding.tvChangeStatus.visibility=View.VISIBLE

            }else if (data.status.equals("cancelled")){
                binding.tvChangeStatus.visibility=View.GONE
            }else if(data.status.equals("working")){
                binding.tvChangeStatus.visibility=View.VISIBLE
            }else if(data.status.equals("completed")){
                binding.tvChangeStatus.visibility=View.GONE
            }

            binding.tvChangeStatus.setOnClickListener { v: View ->


                showChangeStatusDialog(context!!, data.status, position,data.identifier,selector)

            }


            val descriptionString = decryptData(data.remarkInfo.content, appSecret, ivHexString)
            val data = CommonUtils.removeHtmlTags(descriptionString?.toString() ?: "")
            binding.tvContent.text = data
            binding.tvName.text = "$firstName $lastName,$createdDate"


        }

        @SuppressLint("MissingInflatedId")
        private fun showChangeStatusDialog(
            context: FragmentActivity,
            status: String,
            position: Int,
            identifier: String,
            selector: Select,
        ) {
            // Inflate the custom dialog layout
            val dialogView = LayoutInflater.from(context).inflate(R.layout.item_change_status, null)

            val staticData = listOf("Select","Cancelled", "Working", "Completed")

            // Initialize Spinner
            val spinner: Spinner = dialogView.findViewById(R.id.status_data)
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, staticData)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Convert currentStatus to "First Letter Uppercase" format and find the index
            val formattedStatus = status.lowercase().replaceFirstChar { it.uppercase() }


            // Initialize buttons and other views
            val btn_update: Button = dialogView.findViewById(R.id.btn_update)
            val tv: TextView = dialogView.findViewById(R.id.tvHasNextFollowUp)
            val rg: RadioGroup = dialogView.findViewById(R.id.rgFollowUp)
            val etDatetime: EditText = dialogView.findViewById(R.id.tv_datetimepicker)
            val tv_notes: EditText = dialogView.findViewById(R.id.tv_notes)
            val tv_yes: RadioButton = dialogView.findViewById(R.id.rbFollowUpYes)
            val tv_no: RadioButton = dialogView.findViewById(R.id.rbFollowUpNo)
            val ll1: LinearLayout = dialogView.findViewById(R.id.ll3)


            rg.clearCheck()

            // Clear datetime and notes fields
            etDatetime.text.clear()
            tv_notes.text.clear()
            // Create the AlertDialog
            val dialogBuilder = AlertDialog.Builder(context)
                .setView(dialogView)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()


            // Set up Spinner item selection listener
            // Handle spinner selection changes
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedStatus = staticData[position]
                    handleStatusVisibility(context,selectedStatus,tv,rg,ll1)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            rg.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.rbFollowUpNo) {
                    etDatetime.setText("")
                    Toast.makeText(context, "No selected", Toast.LENGTH_SHORT).show()
                    ll1.visibility = View.GONE
                } else {
                    etDatetime.setText("")
                    Toast.makeText(context, "Yes selected", Toast.LENGTH_SHORT).show()
                    ll1.visibility = View.VISIBLE
                }
            }

            etDatetime.setOnClickListener { v: View ->
                showDateTimePickerDialog(etDatetime, context)
            }

            tv_yes.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    tv_no.isChecked = false // Uncheck cbNo if cbYes is checked
                }
            }

            tv_no.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    tv_yes.isChecked = false // Uncheck cbYes if cbNo is checked
                }
            }

            btn_update.setOnClickListener {
                val selectedStatus = spinner.selectedItem.toString()
                val datetime = etDatetime.text.toString()
                val notes = tv_notes.text.toString()
                var checkboxValue = if (tv_yes.isChecked) "Yes" else if (tv_no.isChecked) "No" else ""

                // Check validation based on the selected status
                when (selectedStatus) {
                    "Working" -> {
                        if (checkboxValue.isBlank()) {
                            Toast.makeText(context, "Please select Yes or No", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        if (checkboxValue == "Yes" && datetime.isBlank()) {
                            Toast.makeText(context, "Please select a date and time", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        if (notes.isBlank()) {
                            Toast.makeText(context, "Please enter notes", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                    "Select"->{
                        Toast.makeText(context, "Please select a valid Option", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "Completed", "Cancelled" -> {
                        checkboxValue="no"
                        if (notes.isBlank()) {
                            Toast.makeText(context, "Please enter notes", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                }

                showUpdateStatus(
                    context,
                    selectedStatus.toLowerCase(),
                    checkboxValue.toLowerCase(),
                    datetime,
                    notes,
                    identifier,
                    alertDialog,selector
                )

            }



        }

        fun handleStatusVisibility(
            context: Context,
            status: String,
            tv: TextView,
            rg: RadioGroup,
            ll1: LinearLayout
        ) {

            when (status) {
                "Completed", "Cancelled" -> {
                    tv.visibility = View.GONE
                    rg.visibility = View.GONE
                    ll1.visibility = View.GONE

                }
                "Assigned" -> {
                    tv.visibility = View.VISIBLE
                    rg.visibility = View.VISIBLE
                    ll1.visibility = View.VISIBLE
                }

                "Working" -> {
                    tv.visibility = View.VISIBLE
                    rg.visibility = View.VISIBLE
                    ll1.visibility = View.VISIBLE
                }
                "Select"->{
                    tv.visibility = View.VISIBLE
                    rg.visibility = View.VISIBLE
                    ll1.visibility = View.VISIBLE
                }
            }
        }

        private fun showDateTimePickerDialog(editText: EditText, context: FragmentActivity) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context, R.style.CustomDatePickerDialog,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDateTime = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, selectedDay)
                    }
                    showTimePickerDialog(editText, selectedDateTime, context)
                }, year, month, dayOfMonth
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        private fun showTimePickerDialog(
            editText: EditText,
            selectedDateTime: Calendar,
            context: FragmentActivity
        ) {
            val now = Calendar.getInstance()
            val currentHourOfDay = now.get(Calendar.HOUR_OF_DAY)
            val currentMinute = now.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                context,
                R.style.CustomTimePickerDialog2,
                { _, selectedHourOfDay, selectedMinute ->
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedDateTime.get(Calendar.YEAR))
                        set(Calendar.MONTH, selectedDateTime.get(Calendar.MONTH))
                        set(Calendar.DAY_OF_MONTH, selectedDateTime.get(Calendar.DAY_OF_MONTH))
                        set(Calendar.HOUR_OF_DAY, selectedHourOfDay)
                        set(Calendar.MINUTE, selectedMinute)
                    }

                    if (selectedDateTime.timeInMillis == now.timeInMillis && selectedTime.timeInMillis < now.timeInMillis) {
                        // Show a toast message to notify the user
                        Toast.makeText(
                            context,
                            "Please select a valid future time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Format and set the selected date and time
                        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val formattedDateTime = formatter.format(selectedTime.time)
                        editText.setText(formattedDateTime)
                        editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                    }
                },
                currentHourOfDay,
                currentMinute,
                false // Set is24HourView to false for 12-hour format
            )

            timePickerDialog.updateTime(currentHourOfDay, currentMinute)
            timePickerDialog.show()
        }
    }
}


private fun showUpdateStatus(
    requireActivity: FragmentActivity,
    status: String,
    checkedRadioButtonId: String,
    datetime: String,
    notes: String,
    identifier: String,
    alertDialog: AlertDialog,
    selector: RemindersAdapter.Select,

    ) {


    CommonUtils.accessToken?.let {
        ViewModalClass().postChangeStatusReminderApplicationLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + it,
            identifier,
            status,
            checkedRadioButtonId,
            datetime,
            notes
        ).observe(requireActivity) { response ->
                if (response!!.statusCode == 200) {
                    CommonUtils.toast(
                        requireActivity,
                        response.message ?: "reminder Status Updated Successfully"
                    )

                    selector.onClick()
                    alertDialog.dismiss()

                } else if (response.statusCode == 409) {
                    CommonUtils.toast(requireActivity, response.message ?: "Exists")
                    alertDialog.dismiss()

                } else {
                    CommonUtils.toast(requireActivity, response.message ?: "Notes Failed")
                }
            }

        }


}


