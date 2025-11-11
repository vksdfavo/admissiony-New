package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentEditRelpyBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.EditReplyModel.EditReplyResponse
import com.student.Compass_Abroad.modal.getAllPosts.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class FragmentEditRelpy : BaseFragment() {

private  var  binding: FragmentEditRelpyBinding?=null

    companion object {
        var postIdentifier: Record? = null
        var replyIdentifier: com.student.Compass_Abroad.modal.getCommentReplies.Record? = null
        var reply: String? = null
    }
    var contentKey=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentEditRelpyBinding.inflate(inflater,container,false)
        setData()
        clickListeners()

        return binding!!.root
    }

    private fun clickListeners() {
        binding!!.fabFpBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        binding!!.sendButton.setOnClickListener {
            val editedReply = binding!!.etComment.text.toString().trim()
            if (editedReply.isNotEmpty()) {
                EditedReply(editedReply)
            } else {
                CommonUtils.toast(requireActivity(), "Please type a Reply")
            }
        }
    }

    private fun setData() {
        binding!!.etComment.setText(reply)
    }

    private fun EditedReply(message: String) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        val formData = JSONObject().apply {
            put("content", message)
        }

        val formDataToBeEncrypted = formData.toString()
        val encryptedString = encryptData(formDataToBeEncrypted, appSecret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")

            ViewModalClass().EditReplyLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer " + CommonUtils.accessToken,
                postIdentifier!!.identifier,
                replyIdentifier!!.identifier,
                contentKey
            ).observe(viewLifecycleOwner) { editReplyResponse: EditReplyResponse? ->
                editReplyResponse?.let {
                    if (it.statusCode == 200) {
                        CommonUtils.toast(requireActivity(), it.message ?: "Edit Reply Successful")
                    } else {
                        CommonUtils.toast(requireActivity(), it.message ?: "Edit Reply Failed")
                    }
                }
            }
        } else {
            println("Encryption failed.")
        }
    }

    private fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }
            .joinToString("")
    }
}