package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentPostEditBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.EditPostModel.EditPostResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random

class FragmentPostEdit : BaseFragment() {

private var binding:FragmentPostEditBinding?= null
    companion object{
        var categoryIdentifier:String ?=null
        var message: Spanned?=null
    }

    var editedMessage=""
    var contentKey=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentPostEditBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setUpWebView()
        clickListeners()
        return binding!!.root
    }

    private fun clickListeners() {
        binding!!.fabFpBack.setOnClickListener { v:View->

            requireActivity().onBackPressed()
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding!!.quillViewer.settings.javaScriptEnabled = true
        binding!!.quillViewer.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding!!.quillViewer.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
                }

                return super.onConsoleMessage(consoleMessage)
            }


        }

        binding!!.quillViewer.addJavascriptInterface(WebAppInterface(), "Android")
        binding!!.quillViewer.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {

                super.onPageFinished(view, url)
                // Set initial content here (after page is loaded)
                              binding!!.quillViewer.evaluateJavascript(
                    "window.setQuillContent('${message}')",
                    null
                )
            }
        }
        binding!!.quillViewer.loadUrl("file:///android_asset/quill.html")
        binding!!.sendButton.setOnClickListener {
            // Execute JavaScript code to trigger the click event on the sendButton
            binding!!.quillViewer.evaluateJavascript("document.getElementById('sendButton').click();") { result ->


            }

        }



    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun getTextFromTextArea(content: String) {
            // Handle the received text here
            Log.d("WebView", "Text from textarea: $content")
            // You can perform additional actions with the received content, such as displaying it in a Toast
            // Toast.makeText(requireActivity(), content, Toast.LENGTH_LONG).show()
            editedMessage=content

            EditedPost(editedMessage)
        }



        @JavascriptInterface
        fun setQuillContent(htmlContent: String) {
            Log.d("WebView2222", "Text from textarea: $htmlContent")
        }
    }

    private fun EditedPost(message: String) {
        if (editedMessage.isEmpty()) {
            CommonUtils.toast(requireActivity(), "Please type a message")
        } else {
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


            ViewModalClass().EditPostLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer "+ CommonUtils.accessToken,
                categoryIdentifier.toString(),
                contentKey).observe(requireActivity()) { editPostResponse: EditPostResponse? ->
                editPostResponse?.let { nonNullEditPostModal ->
                    if (nonNullEditPostModal.statusCode == 200) {

                        CommonUtils.toast(
                            requireActivity(),
                            nonNullEditPostModal.message ?: "Edit Post Successful"
                        )
                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullEditPostModal.message ?: "Edit Post Failed"
                        )
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