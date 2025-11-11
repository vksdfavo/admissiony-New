package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.ItemAssignedStaffBinding
import com.student.Compass_Abroad.fragments.home.ViewReviewFragment
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.Data

class AdapterAssignedStaff(
    var context: Context?,
    var applicationAssignedStaffResponse: List<Data>,
    var selector: Select
) : RecyclerView.Adapter<AdapterAssignedStaff.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemAssignedStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    interface Select {
        fun onClick(data: Data?, position1: Int)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = applicationAssignedStaffResponse[position]
        holder.bind(data, context, selector)

        val currentFlavor = BuildConfig.FLAVOR.lowercase()


        if (currentFlavor=="eduways")
        {

            holder.binding!!.tvItemAdAsCall.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return applicationAssignedStaffResponse.size
    }

    class MyViewHolder(
        var binding: ItemAssignedStaffBinding
    ) : RecyclerView.ViewHolder(
        binding.getRoot()
    ) {
        fun bind(
            data: Data,
            context: Context?,
            selector: Select
        ) {



            binding.tvItemName.text =
                "${data.user?.first_name.orEmpty()} ${data.user?.last_name.orEmpty()}"
            binding.tvItemRole.text = data.role?.name.orEmpty()

            binding.pgRating.rating = data.user.average_rating

            val profileUrl = data.user?.profile_picture_url
            if (!profileUrl.isNullOrEmpty()) {
                // Assuming you use a library like Glide or Picasso to load images
                Glide.with(binding.civprofile.context)
                    .load(profileUrl)
                    .placeholder(R.drawable.test_image2) // Optional placeholder image
                    .error(R.drawable.test_image2) // Optional error image
                    .into(binding.civprofile)
            } else {
                binding.civprofile.setImageResource(R.drawable.test_image2) // Set a default image
            }

            binding.tvItemAdAsCall.setOnClickListener {
                val phoneNumber = data.user.mobile // Replace with the actual phone number
                val country_code = data.user.country_code // Replace with the actual phone number
                openDialer(context!!, country_code, phoneNumber.toString())
            }
            binding.btnSeeAllReviews.setOnClickListener { v: View ->

                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                    ViewReviewFragment.data = data.user.identifier

                    v.findNavController().navigate(R.id.viewReviewFragment2)

                } else {
                    ViewReviewFragment.data = data.user.identifier

                    v.findNavController().navigate(R.id.viewReviewFragment)
                }
            }

            if (data.user.is_reviewed == 0) {

                if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

                    binding?.tvItemAdAsCall?.visibility=View.GONE
                    binding?.btnAddReview?.visibility=View.GONE
                    binding?.tvSeparator?.visibility=View.GONE

                }else{

                    binding?.tvItemAdAsCall?.visibility=View.VISIBLE
                    binding?.btnAddReview?.visibility=View.VISIBLE
                    binding?.tvSeparator?.visibility=View.VISIBLE

                }

            } else {
                binding.btnAddReview.visibility = View.GONE
                binding.tvSeparator.visibility = View.GONE
            }

            binding.btnSeeAllReviews.visibility = View.VISIBLE

            binding.btnAddReview.setOnClickListener {
                selector.onClick(data, position)
            }
        }

        fun openDialer(activity: Context, country_code: Int, phoneNumber: String) {
            try {
                // Format the country code to start with '+'
                val formattedCountryCode = if (country_code.toString().startsWith("+")) {
                    country_code.toString()
                } else {
                    "+$country_code"
                }

                val fullNumber = "$formattedCountryCode$phoneNumber"

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$fullNumber")
                activity.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(activity, "Failed to open dialer", Toast.LENGTH_SHORT).show()
            }
        }

    }
}