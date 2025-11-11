package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentAddReplyBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getAllPosts.Record
import com.student.Compass_Abroad.modal.replyModel.ReplyComment

import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class AddFragmentReply : BaseFragment() {

    private lateinit var binding: FragmentAddReplyBinding
    private var reply = ""
    private var contentKey = ""

    companion object {
        var postId: Record? = null
        var commentId: com.student.Compass_Abroad.modal.getAllComments.Record? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddReplyBinding.inflate(inflater, container, false)
        clickListener()
        return binding.root
    }

    private fun clickListener() {
        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.sendButton.setOnClickListener {
            reply = binding.etFwmMessage.text.toString()
            postId?.let { post ->
                commentId?.let { comment ->
                    postReply(comment.identifier, post.identifier, comment)
                } ?: run {
                    CommonUtils.toast(requireActivity(), "Invalid Comment ID")
                }
            } ?: run {
                CommonUtils.toast(requireActivity(), "Invalid Post ID")
            }
        }
    }

    private fun postReply(
        commentId: String,
        postId: String,
        comment: com.student.Compass_Abroad.modal.getAllComments.Record,
    ) {
        if (reply.isEmpty()) {
            CommonUtils.toast(requireActivity(), "Please type a Reply")
            return
        }

        val publicKey = generateRandomHexString(16)
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        val formData = JSONObject().apply {
            put("content", reply)
            put("comment_identifier",commentId)
        }

        val encryptedString = encryptData(formData.toString(), appSecret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            CommonUtils.accessToken?.let { token ->
                ViewModalClass().CreateReplyLiveData(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer $token",
                    postId,
                    contentKey
                ).observe(viewLifecycleOwner) { response ->
                    handleReplyResponse(response)
                }
            } ?: run {
                CommonUtils.toast(requireActivity(), "Invalid Access Token")
            }
        } else {
            CommonUtils.toast(requireActivity(), "Encryption failed")
        }
    }

    private fun handleReplyResponse(response: ReplyComment?) {
        response?.let {
            if (it.statusCode == 201) {
                reply = ""
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                CommonUtils.toast(requireActivity(), it.message ?: "Post Failed")
            }
        }
    }

    private fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }
            .joinToString("")
    }
}