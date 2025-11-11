package com.student.Compass_Abroad.fragments.assignedStaff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterApplicationsDropdown
import com.student.Compass_Abroad.adaptor.AdapterAssignedStaff
import com.student.Compass_Abroad.databinding.FragmentApplicationsStaffBinding
import com.student.Compass_Abroad.fragments.home.ApplicationActiveFragment.Companion.data
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.Data
import com.student.Compass_Abroad.modal.getApplicationResponse.Record
import com.student.Compass_Abroad.modal.getApplicationResponse.getApplicationResponse
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass

class ApplicationsStaffFragment : Fragment() {
    private lateinit var binding: FragmentApplicationsStaffBinding
    private var currentPage = 1
    private var perPage = 25
    private val applicationList: MutableList<Record> = mutableListOf()
    private lateinit var adapterAssignedStaff: AdapterAssignedStaff
    private val applicationAssignedStaffList: MutableList<Data> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentApplicationsStaffBinding.inflate(inflater, container, false)

        getApplicationsList(requireActivity(), binding.applicationDropdown)

        binding.applicationDropdown.setOnClickListener {

            getApplicationsList(requireActivity(), binding.applicationDropdown)

        }

        setupRecyclerView()

        return binding.root
    }

    private fun getApplicationsList(requireActivity: FragmentActivity, et_ApplicationsList: TextView) {
        ViewModalClass().getApplicationResponseLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            currentPage,
            perPage,
            "asc",
            "id",
            data
        ).observe(requireActivity) { destination: getApplicationResponse? ->
            destination?.let { getDestinationCountry ->
                if (getDestinationCountry.statusCode == 200) {
                    applicationList.clear()
                    applicationList.addAll(getDestinationCountry.data!!.records)

                    // ✅ Restore last selected OR fallback to first item
                    if (applicationList.isNotEmpty()) {
                        val lastSelectedId =
                            App.sharedPre?.getString("LAST_SELECTED_APP_ID", null)

                        if (!lastSelectedId.isNullOrEmpty()) {
                            val lastSelectedItem =
                                applicationList.find { it.identifier == lastSelectedId }
                            if (lastSelectedItem != null) {
                                val selectedIndex =
                                    applicationList.indexOf(lastSelectedItem)
                                et_ApplicationsList.text =
                                    "Application ${selectedIndex + 1}"
                                fetchDataFromApi(lastSelectedItem.identifier)
                            } else {
                                // fallback to first item
                                val firstItem = applicationList[0]
                                et_ApplicationsList.text = "Application 1"
                                fetchDataFromApi(firstItem.identifier)
                            }
                        } else {
                            // first time user comes → select first
                            val firstItem = applicationList[0]
                            et_ApplicationsList.text = "Application 1"
                            fetchDataFromApi(firstItem.identifier)
                        }
                    }

                    setDropDownApplicationList(et_ApplicationsList)

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDestinationCountry.message ?: "Failed"
                    )
                }
            }
        }
    }

    private fun setDropDownApplicationList(et_ApplicationsList: TextView) {
        et_ApplicationsList.setOnClickListener {
            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.findViewById<TextView>(R.id.etSelect).setHint("Search Application")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = et_ApplicationsList.width

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterApplicationsDropdown(requireActivity(), applicationList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCountry ->
                val selectedIndex = applicationList.indexOf(selectedCountry)
                et_ApplicationsList.text = "Application ${selectedIndex + 1}"
                fetchDataFromApi(selectedCountry.identifier)

                // ✅ Save last selected identifier
                App.sharedPre?.saveString("LAST_SELECTED_APP_ID",selectedCountry.identifier)


                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(et_ApplicationsList)
            layout.findViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val enteredText = s.toString()
                        adapter.getFilter().filter(enteredText)
                    }
                })
        }
    }

    private fun setupRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFadAs.layoutManager = layoutManager
    }

    private fun fetchDataFromApi(identifier: String) {
        val viewModel = LoginViewModal()
        binding.pbFadAs.visibility = View.VISIBLE

        viewModel.getSingleApplicationAssignStaff(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            identifier
        ).observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val applicationAssignedStaffResponse = it.data

                    if (applicationAssignedStaffResponse.isNullOrEmpty()) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvFadAs.visibility = View.GONE
                    } else {
                        binding.llSaaNoData.visibility = View.GONE
                        binding.rvFadAs.visibility = View.VISIBLE

                        applicationAssignedStaffList.clear()
                        applicationAssignedStaffList.addAll(applicationAssignedStaffResponse)

                        adapterAssignedStaff = AdapterAssignedStaff(
                            activity,
                            applicationAssignedStaffResponse,
                            object : AdapterAssignedStaff.Select {
                                override fun onClick(data: Data?, position1: Int) {
                                    showReviewDialog(requireContext(), data)
                                }
                            })
                        binding.rvFadAs.adapter = adapterAssignedStaff
                        adapterAssignedStaff.notifyDataSetChanged()
                    }
                } else {
                    CommonUtils.toast(
                        requireContext(),
                        it.message ?: "Failed"
                    )
                    binding.llSaaNoData.visibility = View.VISIBLE
                    binding.rvFadAs.visibility = View.GONE
                }
            } ?: run {
                binding.llSaaNoData.visibility = View.VISIBLE
                binding.rvFadAs.visibility = View.GONE
            }
            binding.pbFadAs.visibility = View.GONE
        })
    }

    private fun showReviewDialog(context: Context, data: Data?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_review)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val ivProfile = dialog.findViewById<ImageView>(R.id.ivProfile)
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        val etTitle = dialog.findViewById<EditText>(R.id.et_title)
        val etExperience = dialog.findViewById<EditText>(R.id.et_experience)
        val btnSave = dialog.findViewById<TextView>(R.id.tvSp2_save)
        val btnClose = dialog.findViewById<ImageView>(R.id.back_btn)

        tvName.text = "Leave a review for ${data?.user?.first_name.orEmpty()} ${data?.user?.last_name.orEmpty()}"

        Glide.with(context)
            .load(data?.user?.profile_picture_url)
            .placeholder(R.drawable.test_image2)
            .into(ivProfile)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val experience = etExperience.text.toString().trim()
            val rating = ratingBar.rating

            when {
                title.isEmpty() -> {
                    Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                }

                experience.isEmpty() -> {
                    Toast.makeText(context, "Please enter your experience", Toast.LENGTH_SHORT)
                        .show()
                }

                rating == 0f -> {
                    Toast.makeText(context, "Please provide a rating", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    App.sharedPre!!.getString(AppConstants.User_IDENTIFIER, "")
                        ?.let { userIdentifier ->
                            saveReview(
                                userIdentifier,
                                title,
                                experience,
                                rating.toInt(),
                                dialog
                            )
                        }
                }
            }
        }


        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun saveReview(
        userIdentifier: String,
        title: String,
        content: String,
        rating: Int,
        dialog: Dialog
    ) {

        Log.d("saveReviewNet", userIdentifier + title + content + rating)

        LoginViewModal().saveStaffReview(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty(),
            "Bearer ${CommonUtils.accessToken}",
            App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER, "") ?: "",
            userIdentifier,
            title,
            content,
            rating
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(requireContext(), "Error: Response is null", Toast.LENGTH_SHORT)
                    .show()
                return@observe
            }

            if (response.statusCode == 201) {
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Review submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Log.d(
                "SaveReviewResponse",
                "Status Code: ${response.statusCode}, Message: ${response.message}"
            )
        }
    }
}
