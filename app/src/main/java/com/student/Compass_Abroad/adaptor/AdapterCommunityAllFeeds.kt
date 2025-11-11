package com.student.Compass_Abroad.adaptor

import ReportOptionsAdapter
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.toSpanned
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.CommonUtils.getTimeAgo
import com.student.Compass_Abroad.databinding.ItemCommunityFollowingBinding
import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.home.FragCommunityFollowing
import com.student.Compass_Abroad.fragments.home.FragmentComments
import com.student.Compass_Abroad.fragments.home.FragmentPostEdit
import com.student.Compass_Abroad.fragments.home.ReactionFragment
import com.student.Compass_Abroad.modal.ReportReasons.ReportReasonresponse
import com.student.Compass_Abroad.modal.getAllPosts.Record
import com.student.Compass_Abroad.modal.likePost.LikeResponse
import com.student.Compass_Abroad.modal.reportPost.ReportResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random

class AdapterCommunityAllFeeds(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<Record>,
    var fragCommunityFollowing: FragCommunityFollowing?,
    var selectListener: select?,
) : RecyclerView.Adapter<AdapterCommunityAllFeeds.ViewHolder>() {

    var contentKey = ""


    private var arrayListReason: ArrayList<com.student.Compass_Abroad.modal.ReportReasons.Record> =
        ArrayList()

    interface select {
        fun onCLick(record: Record, position: Int);
        fun onCLick2(record: Record, position: Int);
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemCommunityFollowingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var record = arrayList1[position]
        val profilePictureUrl = arrayList1[position].userInfo?.profile_picture_url

        // Load profile picture
        if (profilePictureUrl == null || profilePictureUrl.toString().isEmpty()) {
            Glide.with(requireActivity)
                .load(R.drawable.test_image2)
                .into(holder.binding.ivItemCommunityAllFeeds)
        } else {
            Glide.with(requireActivity)
                .load(profilePictureUrl.toString())
                .into(holder.binding.ivItemCommunityAllFeeds)
        }

        record.userInfo?.let { userInfo ->
            holder.binding.tvCommunityAllFeedsName.text = userInfo.first_name
        }

        val timeAgo = getTimeAgo(record.created_at)
        holder.binding.tvCommunityAllFeedsTime.text = timeAgo

        val totalLikes = record.reactions.sumBy { it.count }
        holder.binding.tvHelpful.text = "$totalLikes Find Helpful"

        holder.binding.tvComments.text = "${record.commentCount} Comments"

        val publicKey = record.content_key
        val privateKey = AppConstants.privateKey
        val app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        val descriptionString = decryptData(record.content, app_secret, ivHexString)
        holder.binding.tvCommunityAllFeedsMsg.settings.javaScriptEnabled = true
        holder.binding.tvCommunityAllFeedsMsg.loadData(descriptionString.toString(), "text/html", "UTF-8")

        // Toggle visibility based on reaction state
        if (record.reactions[0].is_reacted > 0) {
            holder.binding.ibLiked.visibility = View.VISIBLE
            holder.binding.ibLike.visibility = View.GONE
        } else {
            holder.binding.ibLike.visibility = View.VISIBLE
            holder.binding.ibLiked.visibility = View.GONE
        }

        holder.binding.tvComments.setOnClickListener { v: View ->
            val adapterPosition = holder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                FragmentComments.post = arrayList1[adapterPosition]
                Navigation.findNavController(v).navigate(R.id.fragmentComments)
            }
        }

        holder.binding.tvHelpful.setOnClickListener { v: View ->
            val adapterPosition = holder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                ReactionFragment.LikeReactionDataPass = arrayList1[adapterPosition]
                Navigation.findNavController(v).navigate(R.id.reactionFragment)
            }
        }

        holder.binding.ibLike.setOnClickListener {
            holder.binding.ibLike.isEnabled = false
            likePost(requireActivity, record.identifier, record, selectListener, position, holder)
            toggleLikeButton(holder, true)
        }

        holder.binding.ibLiked.setOnClickListener {
            holder.binding.ibLiked.isEnabled = false
            likePost(requireActivity, record.identifier, record, selectListener, position, holder)
            toggleLikeButton(holder, false)
        }

        holder.binding.ibComments.setOnClickListener { v: View ->
            val adapterPosition = holder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                FragmentComments.post = arrayList1[adapterPosition]
                Navigation.findNavController(v).navigate(R.id.fragmentComments)
            }
        }

        holder.binding.ibCommunityMenu.setOnClickListener { view ->
            if (App.sharedPre?.getString(AppConstants.User_IDENTIFIER, "") != record.userInfo.identifier) {
                showReportPopupMenu(view, record, position)
            } else {
                val popupMenu = PopupMenu(requireActivity, view)
                popupMenu.menuInflater.inflate(R.menu.menu_edit_post, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.title) {
                        "Edit" -> {
                            FragmentPostEdit.categoryIdentifier = record.identifier
                            FragmentPostEdit.message = descriptionString!!.toSpanned()
                            Navigation.findNavController(view).navigate(R.id.fragmentPostEdit)
                            true
                        }
                        "Delete" -> {
                            selectListener!!.onCLick(record, position)
                            notifyDataSetChanged()
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    private fun toggleLikeButton(holder: ViewHolder, isLiked: Boolean) {
        if (isLiked) {
            holder.binding.ibLiked.visibility = View.VISIBLE
            holder.binding.ibLike.visibility = View.GONE
        } else {
            holder.binding.ibLike.visibility = View.VISIBLE
            holder.binding.ibLiked.visibility = View.GONE
        }
    }


    private fun showReportPopupMenu(view: View, record: Record, position: Int) {
        val popupMenu = PopupMenu(requireActivity, view)
        popupMenu.menuInflater.inflate(R.menu.menu_report_user, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Report User") {
                showReportDialog(record)
                true
            } else {
                false
            }
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return arrayList1.size;
    }

    class ViewHolder(
// Use the generated ViewBinding class
        var binding: ItemCommunityFollowingBinding,
    ) : RecyclerView.ViewHolder(
        binding.getRoot()


    )

    private fun showReportDialog(record: Record) {
        // Call the function to get report reasons
        getReasons(requireActivity, record)
    }

    private fun getReasons(activity: FragmentActivity, record: Record) {
        ViewModalClass().getReasonsLiveData(
            activity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken
        ).observe(activity) { response: ReportReasonresponse? ->
            response?.let { reportReasonResponse ->
                if (reportReasonResponse.statusCode == 200) {
                    arrayListReason.clear()
                    arrayListReason.addAll(reportReasonResponse.data?.records ?: emptyList())
                    // Show dialog only if there are reasons
                    if (arrayListReason.isNotEmpty()) {
                        showReportReasonsDialog(requireActivity, record)
                    } else {
                        Toast.makeText(activity, "No reasons found", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(activity, "Failed to get reasons", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showReportReasonsDialog(context: Context, record: Record) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.item_reason_layout, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.rvReasons)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ReportOptionsAdapter(arrayListReason)
        recyclerView.adapter = adapter
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancel)
        val btnReport: Button = dialogView.findViewById(R.id.btnReport)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        btnReport.setOnClickListener {
            val selectedReasonIdentifier = adapter.getSelectedReasonIdentifier()
            if (selectedReasonIdentifier != null) {
                reportPost(record.identifier, selectedReasonIdentifier)
                alertDialog.dismiss()
            } else {
                Toast.makeText(context, "Please select a reason", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun reportPost(postidentifier: String, selectedReasonIdentifier: String) {

        val hexString = generateRandomHexString(16)
        var publicKey = hexString
        var privateKey = AppConstants.privateKey
        var app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"


        //form data with email login code start
        val formData = JSONObject();

        //fix form fields
        //email or phone
        //get from login screen
        formData.put("report_reason_identifier", selectedReasonIdentifier) //get from login screen


        val formDataToBeEncrypted = formData.toString();
        val encryptedString = encryptData(formDataToBeEncrypted, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
        } else {
            println("Encryption failed.")
        }

        ViewModalClass().ReportResponseLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            postidentifier,
            contentKey
        ).observe(requireActivity) { reportResponseResponse: ReportResponse? ->
            reportResponseResponse?.let { nonNullreportResponseResponse ->
                if (nonNullreportResponseResponse.statusCode == 201) {


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        nonNullreportResponseResponse.message ?: "Report Post Failed"
                    )
                }
            }
        }
    }
}

private fun likePost(
    requireActivity: FragmentActivity,
    identifier: String,
    record: Record,
    selectListener: AdapterCommunityAllFeeds.select?,
    position: Int,
    holder: AdapterCommunityAllFeeds.ViewHolder
) {
    var likeKey = ""
    val hexString = generateRandomHexString(16)
    var publicKey = hexString
    var privateKey = AppConstants.privateKey
    var app_secret = AppConstants.appSecret
    val ivHexString = "$privateKey$publicKey"

    //form data with email login code start
    val formData = JSONObject();

    formData.put("reaction", "like") //get from login screen


    val formDataToBeEncrypted = formData.toString();
    val encryptedString = encryptData(formDataToBeEncrypted, app_secret, ivHexString)
    if (encryptedString != null) {
        likeKey = "$publicKey^#^$encryptedString"
        println("Encrypted data: $encryptedString")
    } else {
        println("Encryption failed.")
    }
    holder.binding.ibLike.isEnabled = false
    holder.binding.ibLiked.isEnabled = false

    ViewModalClass().likeResponseLiveData(
        requireActivity,
        AppConstants.fiClientNumber,
        App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
        "Bearer " + CommonUtils.accessToken,
        identifier,
        likeKey
    ).observe(requireActivity) { likeResponse: LikeResponse? ->
        likeResponse?.let { nonNulllikeResponseResponse ->
            if (likeResponse.statusCode == 200) {
                holder.binding.ibLike.isEnabled = true
                holder.binding.ibLiked.isEnabled = true

                selectListener!!.onCLick2(record, position)
            } else {
                CommonUtils.toast(
                    requireActivity,
                    likeResponse.message ?: "like Post Failed"
                )
            }
        }
    }

}
fun generateRandomHexString(length: Int): String {
    val hexChars = "0123456789abcdef"
    return (1..length)
        .map { hexChars[Random.nextInt(hexChars.length)] }

        .joinToString("")
}




