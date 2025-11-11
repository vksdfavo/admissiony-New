package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.RemindersAdapter
import com.student.Compass_Abroad.databinding.FragmentRemindersBinding
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.fragments.BaseFragment
import java.text.DecimalFormat
import java.util.Calendar

class FragmentReminders : BaseFragment() {

    private lateinit var binding: FragmentRemindersBinding
    private lateinit var reminderAdapter: RemindersAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val applicationReminderList: MutableList<com.student.Compass_Abroad.modal.getApplicationRemider.Record> = mutableListOf()
    private var currentPage = 1
    private var isLastPage = false
    private val perPage = 10
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }

    companion object {

        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchDataFromApi(currentPage)
        setClickListeners()

        data?.let { Log.d("MissingInflatedId", it.identifier) }
        return binding.root
    }

    @SuppressLint("MissingInflatedId")
    private fun setClickListeners() {
        binding.fabAddReminder.setOnClickListener {
            showBottomSheetReminder()

        }
    }

    private fun showBottomSheetReminder() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_reminder, null)
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheet2)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val imageView: ImageView = bottomSheetView.findViewById(R.id.back_btn)
        val dateEditText: EditText = bottomSheetView.findViewById(R.id.et_date_reminder)
        val timeEditText: EditText = bottomSheetView.findViewById(R.id.et_time)
        val etNotes: EditText = bottomSheetView.findViewById(R.id.et_notes)

        imageView.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dateEditText.setOnClickListener {
            it.clearFocus()
            showDatePickerDialog(dateEditText)
            timeEditText.setText("")
        }

        timeEditText.setOnClickListener {
            it.clearFocus()
            showTimePickerDialog(timeEditText,dateEditText.text.toString())
        }

        bottomSheetView.findViewById<View>(R.id.tvSp2_save).setOnClickListener {
            val selectedDate = dateEditText.text.toString()
            val selectedTime = timeEditText.text.toString()
            val notes = etNotes.text.toString()

            if (validateInputs(selectedDate, selectedTime, notes)) {
                val dateTime = "$selectedDate $selectedTime"
                postReminder(dateTime, notes)
            }
        }
    }

    private fun validateInputs(date: String, time: String, notes: String): Boolean {
        return when {
            date.isEmpty() -> {
                showToast("Please select a date")
                false
            }
            time.isEmpty() -> {
                showToast("Please select a time")
                false
            }
            notes.isEmpty() -> {
                showToast("Please enter notes")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.layoutManager = layoutManager

        reminderAdapter = RemindersAdapter(requireActivity(), applicationReminderList,object:RemindersAdapter.Select{
            override fun onClick() {
                applicationReminderList.clear()
                currentPage=1
                fetchDataFromApi(currentPage)
            }

        })
        binding.rv.adapter = reminderAdapter

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!binding.pbPagination.isShown && !isLastPage &&
                    firstVisibleItemPosition + visibleItemCount >= totalItemCount && dy > 0) {
                    currentPage++
                    fetchDataFromApi(currentPage)
                }
            }
        })
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(), R.style.CustomDatePickerDialog,
            { _, y, m, d ->
                val selectedYear = y
                val selectedMonth = m + 1
                val selectedDayOfMonth = d
                val formatter = DecimalFormat("00")
                val text = "$selectedYear-${formatter.format(selectedMonth)}-${formatter.format(selectedDayOfMonth)}"
                editText.setText(text)
                editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
            }, year, month, dayOfMonth
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText, selectedDate: String) {
        val calendar = Calendar.getInstance()
        val currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)

        try {
            val selectedYear = selectedDate.substring(0, 4).toInt()
            val selectedMonth = selectedDate.substring(5, 7).toInt() - 1
            val selectedDay = selectedDate.substring(8, 10).toInt()

            val selectedDateTime = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }

            val timePickerDialog = TimePickerDialog(
                requireContext(), R.style.CustomTimePickerDialog2,
                { _, selectedHourOfDay, selectedMinute ->
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHourOfDay)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, currentSecond)
                    }

                    if (selectedDateTime.timeInMillis == calendar.timeInMillis && selectedTime.timeInMillis < calendar.timeInMillis) {
                        val formatter = DecimalFormat("00")
                        val currentTimeText = "${formatter.format(currentHourOfDay)}:${formatter.format(currentMinute)}:${formatter.format(currentSecond)} ${if (currentHourOfDay >= 12) "PM" else "AM"}"
                        editText.setText(currentTimeText)
                        editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                    } else {
                        val isPM = selectedHourOfDay >= 12
                        val hour = if (selectedHourOfDay > 12) selectedHourOfDay - 12 else selectedHourOfDay
                        val formatter = DecimalFormat("00")
                        val timeText = "${formatter.format(hour)}:${formatter.format(selectedMinute)}:${formatter.format(currentSecond)} ${if (isPM) "PM" else "AM"}"
                        editText.setText(timeText)
                        editText.setBackgroundResource(R.drawable.shape_rectangle_all_radius_et_login)
                    }
                },
                currentHourOfDay, currentMinute, false
            )

            timePickerDialog.updateTime(currentHourOfDay, currentMinute)
            timePickerDialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Invalid date format. Please select a valid date.")
        }
    }

    private fun fetchDataFromApi(page: Int) {
        if (page == 1) {
            binding.pbReminder.visibility = View.VISIBLE
        } else {
            binding.pbPagination.visibility = View.VISIBLE
        }

        viewModel.getApplicationReminderResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            "reminder",
            "application",
            data?.identifier ?: "",
            page,
            perPage,
            "asc",
            "id"
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val applicationReminder = it.data?.records ?: emptyList()
                    val metaInfo = it.data?.metaInfo

                    if (page == 1) {
                        applicationReminderList.clear()
                    }

                    applicationReminderList.addAll(applicationReminder)
                    reminderAdapter.notifyDataSetChanged()

                    isLastPage = metaInfo?.hasNextPage ?: false

                    updateUIBasedOnData()
                } else {
                    handleApiError(it.message ?: "Failed")
                }
            } ?: run {
                handleApiError("Failed")
            }

            if (page == 1) {
                binding.pbReminder.visibility = View.GONE
            } else {
                binding.pbPagination.visibility = View.INVISIBLE
            }
        }
    }

    private fun handleApiError(message: String) {
        showToast(message)
        if (currentPage == 1) {
            binding.llSaaNoData.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
        }
    }

    private fun updateUIBasedOnData() {
        if (applicationReminderList.isEmpty()) {
            binding.llSaaNoData.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
        } else {
            binding.llSaaNoData.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE
        }
    }

    private fun postReminder( dateTime: String, notes: String) {
        CommonUtils.accessToken?.let {
            ViewModalClass().postReminderLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer " + it,
                "application",
                data!!.identifier,
                dateTime,
                notes
            ).observe(requireActivity()) { postReminderResponse ->
                postReminderResponse?.let { response ->
                    if (response.statusCode == 201) {
                        CommonUtils.toast(requireActivity(), response.message ?: "Reminder Created Successfully")
                        applicationReminderList.clear()
                        reminderAdapter.notifyDataSetChanged()
                        currentPage=1
                        fetchDataFromApi(currentPage)
                        bottomSheetDialog.dismiss()
                    }else if(response.statusCode==409){
                        CommonUtils.toast(requireActivity(), response.message ?: "Exists")
                        bottomSheetDialog.dismiss()
                    } else {
                        CommonUtils.toast(requireActivity(), response.message ?: "reminder Failed")
                    }
                }

            }
        }

    }


}