package com.student.Compass_Abroad

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterDestinationCountrySelector
import com.student.Compass_Abroad.adaptor.AdapterGetStaffList
import com.student.Compass_Abroad.databinding.FragmentBookCouncellingBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.getStaffSlots.GetStaffSlots
import com.student.Compass_Abroad.modal.getStaffSlots.Slot
import com.student.Compass_Abroad.retrofit.LoginViewModal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookCounsellingFragment : BaseFragment() {
    private lateinit var binding: FragmentBookCouncellingBinding
    private lateinit var dateAdapter: DateAdapter
    private val dateList = mutableListOf<DateItem>()
    private lateinit var timeSlotAdapter: TimeSlotAdapter

    var staffId: String? = null
    var branch_id: String? = null

    private val arrayListStaff: MutableList<com.student.Compass_Abroad.modal.getStaffList.RecordsInfo> = mutableListOf()
    private val arrayListBranch: MutableList<com.student.Compass_Abroad.modal.getDestinationCountryList.Data> = mutableListOf()
    private val arrayListSlots: MutableList<Slot> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookCouncellingBinding.inflate(inflater, container, false)

        setupDates()
        setupRecyclerView()
        setupTimeSlotRecycler()

        getSlotesData()

        getBranchListList(requireActivity(), binding.branchTxt)

        binding.branchTxt.setOnClickListener {
            getBranchListList(requireActivity(), binding.branchTxt)
        }

        binding.staffTxt.setOnClickListener {
            if (branch_id.isNullOrEmpty()) {
                CommonUtils.toast(requireActivity(), "Please select branch first")
            } else if (arrayListStaff.isEmpty()) {
                CommonUtils.toast(requireActivity(), "No staff available for this branch")
            } else {
                setDropDownPreferStaffList(binding.staffTxt)
            }
        }

        binding.fabBiBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

//        binding.btnConfirm.setOnClickListener {
//            val selectedDate = dateList.find { it.isSelected }?.apiDate
//            val selectedSlot = timeSlotAdapter.getSelectedSlot()
//
//            if (selectedDate.isNullOrEmpty() || selectedSlot == null) {
//                CommonUtils.toast(requireActivity(), "Please select a date and time slot")
//                return@setOnClickListener
//            }
//
//            try {
//                // ✅ Correct format from API: "2025-11-04 09:00"
//                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//                val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//
//                val start = inputFormat.parse("$selectedDate ${selectedSlot.start_time}")
//                val end = inputFormat.parse("$selectedDate ${selectedSlot.end_time}")
//
//                val eventStart = outputFormat.format(start!!)
//                val eventEnd = outputFormat.format(end!!)
//
//                Log.d("API_FORMAT", "event_start_datetime=$eventStart, event_end_datetime=$eventEnd")
//
//                LoginViewModal().CreateSlots(
//                    activity = requireActivity(),
//                    clientNumber = AppConstants.fiClientNumber,
//                    deviceNumber = App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty(),
//                    accessToken = "Bearer ${CommonUtils.accessToken}",
//                    branch_identifier = branch_id.toString(),
//                    event_start_datetime = eventStart,
//                    event_end_datetime = eventEnd
//                ).observe(requireActivity()) { response ->
//                    if (response != null && response.success) {
//                        CommonUtils.toast(requireActivity(), "✅ Slot booked successfully")
//                        Log.d("BOOKING_API", "Success: ${response.message}")
//                    } else {
//                        CommonUtils.toast(requireActivity(), response?.message ?: "Something went wrong")
//                        Log.e("BOOKING_API", "Error: ${response?.message}")
//                    }
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                CommonUtils.toast(requireActivity(), "Invalid date format")
//                Log.e("DateParseError", "Error: ${e.message}")
//            }
//        }

        return binding.root
    }

    // ---------------------- Setup Recyclers ----------------------
    private fun setupTimeSlotRecycler() {
        timeSlotAdapter = TimeSlotAdapter(arrayListSlots) { selectedSlot ->
            val selectedDate = dateList.find { it.isSelected }?.apiDate ?: return@TimeSlotAdapter
            if (selectedSlot != null) {
                val postBody = mapOf(
                    "date" to selectedDate,
                    "start_time" to selectedSlot.start_time,
                    "end_time" to selectedSlot.end_time
                )
                Log.d("BookCounselling", "POST_BODY: $postBody")
            } else {
                Log.d("BookCounselling", "No slot selected")
            }
        }

        binding.recyclerViewTimeSlots.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = timeSlotAdapter
        }
    }

    private fun setupRecyclerView() {
        dateAdapter = DateAdapter(dateList) { selectedIndex ->
            dateList.forEachIndexed { index, item -> item.isSelected = index == selectedIndex }
            dateAdapter.notifyDataSetChanged()

            val selectedDate = dateList[selectedIndex]
            Log.d("BookCounselling", "BranchId: $branch_id, StaffId: $staffId, Date: ${selectedDate.apiDate}")

            if (!branch_id.isNullOrEmpty() || !staffId.isNullOrEmpty()) {
                //getSlotesData(selectedDate.apiDate)
            } else {
                showNoSlotsMessage(getString(R.string.staff_and_date_are_required_for_time_slots))
            }
        }

        binding.recyclerViewDates.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = dateAdapter
        }
    }

    // ---------------------- Date Setup ----------------------
    private fun setupDates() {
        val sdfLabel = SimpleDateFormat("dd MMM", Locale.getDefault())
        val sdfApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (i in 0 until 30) {
            val label = when (i) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ?: ""
            }
            val dateLabel = sdfLabel.format(calendar.time)
            val apiDate = sdfApi.format(calendar.time)
            val isSunday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

            dateList.add(
                DateItem(
                    label = label,
                    date = dateLabel,
                    apiDate = apiDate,
                    isSelected = i == 0,
                    isSunday = isSunday
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    // ---------------------- Branch Dropdown ----------------------
    private fun getBranchListList(requireActivity: FragmentActivity, et_Branch: TextView) {
        LoginViewModal().get_branchList(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty(),
            "Bearer ${CommonUtils.accessToken}"
        ).observe(requireActivity) { destination: getDestinationCountry? ->
            destination?.let { getBranchList ->
                if (getBranchList.statusCode == 200) {
                    getBranchList.data?.let {
                        arrayListBranch.clear()
                        arrayListBranch.addAll(it)
                    } ?: run {
                        CommonUtils.toast(requireActivity(), "Failed to retrieve BranchList. Please try again.")
                    }
                    setDropDownBranchListList(et_Branch)
                } else {
                    CommonUtils.toast(requireActivity, getBranchList.message ?: "Failed")
                }
            }
        }
    }

    private fun setDropDownBranchListList(etBranch: TextView) {
        etBranch.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout
            layout.findViewById<TextView>(R.id.etSelect).setHint("Search nearest branch")
            popupWindow.setBackgroundDrawable(Color.WHITE.toDrawable())
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = etBranch.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterDestinationCountrySelector(requireActivity(), arrayListBranch, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedBranch ->
                etBranch.text = selectedBranch.label
                branch_id = selectedBranch.value.toString()

                arrayListStaff.clear()
                staffId = ""
                binding.staffTxt.text = "Select"

                popupWindow.dismiss()

                // 🔹 Automatically load today's slots after branch selection
                val today = dateList.firstOrNull { it.label == "Today" }
                today?.let {
                    //getSlotesData(it.apiDate)
                }
            }

            popupWindow.showAsDropDown(etBranch)

            layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    adapter.getFilter().filter(s.toString())
                }
            })
        }
    }

    // ---------------------- Staff Dropdown ----------------------
    private fun setDropDownPreferStaffList(preferCollage: TextView) {
        val popupWindow = PopupWindow(requireActivity())
        val layout: View = LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
        popupWindow.contentView = layout
        layout.findViewById<TextView>(R.id.etSelect).setHint("Search staff")
        popupWindow.setBackgroundDrawable(Color.WHITE.toDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.elevation = 5f
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.width = preferCollage.width

        val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterGetStaffList(requireActivity(), arrayListStaff, layout)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { selectedStaff ->
            preferCollage.text = selectedStaff.label
            staffId = selectedStaff.value.toString()
            popupWindow.dismiss()

            val today = dateList.firstOrNull { it.label == "Today" }
            today?.let {
                //getSlotesData(it.apiDate)
            }
        }

        popupWindow.showAsDropDown(preferCollage)

        layout.findViewById<EditText>(R.id.etSelect).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                adapter.getFilter().filter(s.toString())
            }
        })
    }

    // ---------------------- Get Slots ----------------------
    private fun getSlotesData() {
        arrayListSlots.clear()

        val staticSlots = listOf(
            Slot("",start_time = "09:00", end_time = "09:30", is_booked = 0, is_available = 1),
            Slot("",start_time = "09:30", end_time = "10:00", is_booked = 0, is_available = 1),
            Slot("",start_time = "10:00", end_time = "10:30", is_booked = 1, is_available = 1), // booked
            Slot("",start_time = "10:30", end_time = "11:00", is_booked = 0, is_available = 0), // unavailable
            Slot("",start_time = "11:00", end_time = "11:30", is_booked = 0, is_available = 1),
            Slot("", start_time = "11:30", end_time = "12:00", is_booked = 0, is_available = 1)
        )

        val adapter = TimeSlotAdapter(staticSlots) { selectedSlot ->
            // Handle selection
            if (selectedSlot != null) {
                Toast.makeText(requireContext(), "Selected: ${selectedSlot.start_time} - ${selectedSlot.end_time}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Selection cleared", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerViewTimeSlots.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3) // 🔹 2 per row
            this.adapter = adapter
        }


//        binding.tvStaffDateRequired.visibility = View.VISIBLE
//        binding.tvStaffDateRequired.text = "Loading time slots..."
//
//        LoginViewModal().get_staffSlots(
//            requireActivity(),
//            AppConstants.fiClientNumber,
//            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty(),
//            "Bearer ${CommonUtils.accessToken}",
//            apiDate,
//            branch_id.toString()
//        ).observe(requireActivity()) { response: GetStaffSlots? ->
//            response?.let { result ->
//                if (result.statusCode == 200) {
//                    result.data?.slots?.let { slots ->
//                        arrayListSlots.addAll(slots)
//                        if (arrayListSlots.isNotEmpty()) {
//                            timeSlotAdapter.clearSelection()
//                            timeSlotAdapter.notifyDataSetChanged()
//                            binding.tvStaffDateRequired.visibility = View.GONE
//                            binding.recyclerViewTimeSlots.visibility = View.VISIBLE
//                        } else {
//                            showNoSlotsMessage("⚠ No time slots available")
//                        }
//                    } ?: run {
//                        showNoSlotsMessage("⚠ No slots available")
//                    }
//                } else {
//                    showNoSlotsMessage(getString(R.string.staff_and_date_are_required_for_time_slots))
//                }
//            }
//        }
    }

    private fun showNoSlotsMessage(message: String) {
        binding.tvStaffDateRequired.visibility = View.VISIBLE
        binding.tvStaffDateRequired.text = message
        binding.recyclerViewTimeSlots.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible = false
    }
}
