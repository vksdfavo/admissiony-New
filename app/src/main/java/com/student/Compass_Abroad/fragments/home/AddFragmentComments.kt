package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentAddBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class AddFragmentComments : BaseFragment() {

private lateinit var  binding:FragmentAddBinding
    var comment=""
    var contentKey=""

    companion object{
        var data:com.student.Compass_Abroad.modal.getAllPosts.Record? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentAddBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment


        clickListener()
        return binding.root
    }
    private fun clickListener() {
        binding.fabFpBack.setOnClickListener { v:View->
            requireActivity()!!.onBackPressed()
        }

        binding.sendButton.setOnClickListener {
             comment = binding.etFwmMessage.text.toString()
            PostsComment(comment)
        }
    }

    private fun PostsComment(message: String) {
         if(comment.isEmpty()){
            CommonUtils.toast(requireActivity(),"Please type a Comment")
        }
        else {
            val hexString = generateRandomHexString(16)
            var publicKey = hexString
            var privateKey = AppConstants.privateKey
            var app_secret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"


            //form data with email login code start
            val formData = JSONObject();

            //fix form fields
           //email or phone
            formData.put("content", message) //get from login screen


            val formDataToBeEncrypted = formData.toString();
            val encryptedString = encryptData(formDataToBeEncrypted, app_secret, ivHexString)
            if (encryptedString != null) {
                contentKey = "$publicKey^#^$encryptedString"
                println("Encrypted data: $encryptedString")
            } else {
                println("Encryption failed.")
            }

            CommonUtils.accessToken?.let {
                ViewModalClass().CreateCommentLiveData(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer "+it,
                    data!!.identifier,
                    contentKey
                ).observe(requireActivity()) { PostComment ->
                    PostComment?.let { response ->
                        if (response.statusCode == 201) {
                           if(isAdded){
                               CommonUtils.toast(requireActivity(), response.message ?: "Comment Created Successfully")
                               comment=""
                               requireActivity().onBackPressedDispatcher.onBackPressed()
                           }

                        } else {
                            CommonUtils.toast(requireActivity(), response.message ?: "Post Failed")
                        }
                    }
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




}