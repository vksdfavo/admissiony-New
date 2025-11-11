package com.student.Compass_Abroad.fragments.home

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterAssignedStaff
import com.student.Compass_Abroad.adaptor.ProgramAdapter
import com.student.Compass_Abroad.databinding.FragmentFragApplicationAssignedStaffBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.SaveReviewResponse.SaveReviewResponse
import com.student.Compass_Abroad.modal.editProfile.EditProfile
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.Data
import com.student.Compass_Abroad.retrofit.ViewModalClass


class FragApplicationAssignedStaff : BaseFragment() {

    private lateinit var binding: FragmentFragApplicationAssignedStaffBinding
    private lateinit var adapterAssignedStaff: AdapterAssignedStaff
    private val applicationAssignedStaffList: MutableList<Data> = mutableListOf()
    companion object {

        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentFragApplicationAssignedStaffBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        setupRecyclerView()
        fetchDataFromApi()
        return binding.root
    }



    private fun setupRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvFadAs.setLayoutManager(layoutManager)

    }

    private fun fetchDataFromApi() {
        val viewModel = ViewModalClass()

        // Replace with your actual data identifier or handle null case
        val identifier = FragApplicationAssignedStaff.data?.identifier ?: ""

        // Show progress bar
        binding.pbFadAs.visibility = View.VISIBLE

        viewModel.getApplicationAssignedStaffResponseResponseLiveData(
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

                        // Initialize adapter with fetched data
                        adapterAssignedStaff = AdapterAssignedStaff(activity, applicationAssignedStaffResponse,object:
                            AdapterAssignedStaff.Select{
                            override fun onClick(data: Data?, position1: Int) {
                                showReviewDialog(context!!,data)
                            }

                        })
                        binding.rvFadAs.adapter = adapterAssignedStaff

                        adapterAssignedStaff.notifyDataSetChanged() // Notify adapter of dataset change
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
            binding.pbFadAs.visibility = View.GONE // Hide progress bar
        })
        
        
        
    }
    private fun showReviewDialog(context: Context, data: Data?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_review) // Ensure `dialog_review.xml` has a ScrollView
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
                    Toast.makeText(context, "Please enter your experience", Toast.LENGTH_SHORT).show()
                }
                rating == 0f -> {
                    Toast.makeText(context, "Please provide a rating", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val identifier = FragApplicationAssignedStaff.data?.identifier ?: ""
                    saveReview(data?.user?.identifier.orEmpty(), title, experience, rating.toInt(), dialog)

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
        val identifier = data?.identifier.orEmpty()
        if (identifier.isEmpty() || userIdentifier.isEmpty()) {
            Toast.makeText(requireContext(), "Invalid data, please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        ViewModalClass().saveReviewLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty(),
            "Bearer ${CommonUtils.accessToken}",
            identifier,
            userIdentifier,
            title,
            content,
            rating
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(requireContext(), "Error: Response is null", Toast.LENGTH_SHORT).show()
                return@observe
            }

            // Check if status code is 201
            if (response.statusCode == 201) {
                Toast.makeText(requireContext(), response.message ?: "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                fetchDataFromApi()
            } else {
                Toast.makeText(requireContext(), response.message ?: "Failed to submit review", Toast.LENGTH_SHORT).show()
            }

            // Log response for debugging
            Log.d("SaveReviewResponse", "Status Code: ${response.statusCode}, Message: ${response.message}")
        }
    }

}






