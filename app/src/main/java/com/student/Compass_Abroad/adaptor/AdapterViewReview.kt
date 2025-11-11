package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemViewReviewBinding
import com.student.Compass_Abroad.modal.getReviewList.Record

class AdapterViewReview(
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<AdapterViewReview.MyViewHolder>() {

    private var viewReviewList: MutableList<Record> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemViewReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val records = viewReviewList[position]
        holder.bind(records, requireActivity)
    }

    override fun getItemCount(): Int {
        return viewReviewList.size
    }

    class MyViewHolder(
        private val binding: ItemViewReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(records: Record, requireActivity: FragmentActivity) {
            val name =
                "${records.review_by_user_info.first_name} ${records.review_by_user_info.last_name}"
            binding.tvItemName.text = if (name.isNotBlank()) name else "--"

            val image = records.review_by_user_info.profile_picture_url
            if (image != null && image.isNotEmpty()) {
                Glide.with(requireActivity)
                    .load(image)
                    .into(binding.civprofile)
            } else {
                binding.civprofile.setImageResource(R.drawable.test_image2)
            }

            val rating = records.rating.toString()
            if (rating.isNotEmpty()) {
                binding.pgRating.rating = rating.toFloat()
            }

            val tvDes = records.review_by_user_role_info.name
            if (tvDes.isNotEmpty()) {
                binding.tvItemRole.text = tvDes
            }
        }
    }

    fun addReviews(newReviews: List<Record>) {
        val startPosition = viewReviewList.size
        viewReviewList.addAll(newReviews)
        notifyItemRangeInserted(startPosition, newReviews.size)
    }
}