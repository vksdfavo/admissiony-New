package com.student.Compass_Abroad.adaptor

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.appcompat.widget.PopupMenu
    import androidx.fragment.app.FragmentActivity
    import androidx.navigation.Navigation
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.student.Compass_Abroad.R
    import com.student.Compass_Abroad.Utils.AppConstants
    import com.student.Compass_Abroad.Utils.CommonUtils
    import com.student.Compass_Abroad.databinding.ItemReplyBinding
    import com.student.Compass_Abroad.encrytion.decryptData
    import com.student.Compass_Abroad.fragments.home.FragmentEditRelpy
    import com.student.Compass_Abroad.modal.getCommentReplies.Record
    import java.util.ArrayList

class AdapterCommunityPostReply(
    var requireActivity: FragmentActivity,
    var arrayListCommentReplies: ArrayList<Record>,
    var post: com.student.Compass_Abroad.modal.getAllPosts.Record?,
    var selectListener:select?,

    ) :RecyclerView.Adapter<AdapterCommunityPostReply.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=arrayListCommentReplies[position]

        holder.bind(currentItem)

        val publicKey = currentItem.content_key
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val descriptionString = decryptData(currentItem.content, appSecret, ivHexString)
        val data = CommonUtils.removeHtmlTags(descriptionString.toString())



        holder.binding.ibCommunityMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireActivity, view)
            popupMenu.menuInflater.inflate(R.menu.menu_edit_post, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "Edit") {
                    FragmentEditRelpy.postIdentifier = post
                    FragmentEditRelpy.replyIdentifier =  currentItem
                    FragmentEditRelpy.reply = data.toString()
                    Navigation.findNavController(view).navigate(R.id.fragmentEditRelpy)
                    true
                } else if (menuItem.title == "Delete") {
                    selectListener!!.onCLick(currentItem,post)
                    notifyDataSetChanged()
                    true
                } else {
                    false
                }
            }
            popupMenu.show()
        }

    }

    interface select {
        fun onCLick(record: Record, post: com.student.Compass_Abroad.modal.getAllPosts.Record?);
    }
    override fun getItemCount(): Int {
        return arrayListCommentReplies.size
    }


    class MyViewHolder(
        var binding: ItemReplyBinding
    ) : RecyclerView.ViewHolder(
        binding.getRoot()) {

        fun bind(record: Record) {
            val profilePictureUrl = record.userInfo.profile_picture_url

            if (profilePictureUrl.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(R.drawable.test_image2)
                    .into(binding.civItemComments)
            } else {
                Glide.with(binding.root)
                    .load(profilePictureUrl)
                    .into(binding.civItemComments)
            }

            binding.tvItemCommentsName.text = record.userInfo.first_name
            val timeAgo = CommonUtils.getTimeAgo(record.created_at)
            binding.tvItemCommentsTime.text = timeAgo



            val publicKey = record.content_key
            val privateKey = AppConstants.privateKey
            val appSecret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"
            val descriptionString = decryptData(record.content, appSecret, ivHexString)
            val data = CommonUtils.removeHtmlTags(descriptionString.toString())


            binding.tvItemCommentsMsg.text = data


        }
        }
}