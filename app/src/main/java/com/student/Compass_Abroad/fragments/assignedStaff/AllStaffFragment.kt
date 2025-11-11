package com.student.Compass_Abroad.fragments.assignedStaff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterAssignedStaff
import com.student.Compass_Abroad.databinding.FragmentAllStaffBinding
import com.student.Compass_Abroad.fragments.home.FragApplicationAssignedStaff
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.Data
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass

class AllStaffFragment : Fragment() {
    private lateinit var binding: FragmentAllStaffBinding
    private lateinit var adapterAssignedStaff: AdapterAssignedStaff
    private val applicationAssignedStaffList: MutableList<Data> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentAllStaffBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchDataFromApi()

        return binding.root

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFadAs.setLayoutManager(layoutManager)

    }
    private fun fetchDataFromApi() {
        val viewModel = ViewModalClass()

        // Show progress bar
        binding.pbFadAs.visibility = View.VISIBLE

        viewModel.getApplicationAssignedStaffResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER, "") ?: ""
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

                        // Initialize adapter with fetched data
                        adapterAssignedStaff = AdapterAssignedStaff(
                            activity,
                            applicationAssignedStaffResponse,
                            object :
                                AdapterAssignedStaff.Select {
                                override fun onClick(data: Data?, position1: Int) {
                                    showReviewDialog(context!!, data)
                                }

                            })
                        binding.rvFadAs.adapter = adapterAssignedStaff

                        adapterAssignedStaff.notifyDataSetChanged()
                    }
                } else {
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
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
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val ivProfile = dialog.findViewById<ImageView>(R.id.ivProfile)
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        val etTitle = dialog.findViewById<EditText>(R.id.et_title)
        val etExperience = dialog.findViewById<EditText>(R.id.et_experience)
        val btnSave = dialog.findViewById<TextView>(R.id.tvSp2_save)
        val btnClose = dialog.findViewById<ImageView>(R.id.back_btn)

        // Set user data
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
        dialog: Dialog)
    {

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
                ).show() }

            Log.d(
                "SaveReviewResponse",
                "Status Code: ${response.statusCode}, Message: ${response.message}"
            )
        }
    }


}