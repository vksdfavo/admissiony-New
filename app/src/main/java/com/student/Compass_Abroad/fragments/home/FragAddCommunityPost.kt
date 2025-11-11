package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterCategoriesSelector
import com.student.Compass_Abroad.databinding.FragmentFragAddCommunityPostBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.CommunityCategories.CategoriesResponse

import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class FragAddCommunityPost : BaseFragment() {

    private lateinit var binding: FragmentFragAddCommunityPostBinding
    private var arrayList=ArrayList<com.student.Compass_Abroad.modal.CommunityCategories.Record>()
    var fragment:Fragment?=null
    private var xLocationOfView=0
    private var yLocationOfView=0
    private var valueIdentifier=""
    var contentKey=""
    var  categoryMessage=""
    private var chatMessage= ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentFragAddCommunityPostBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        setUpWebView()

        getCategories(requireActivity())
        clickListener()

        Log.d("device_number", App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "")
        Log.d("device_number", CommonUtils.accessToken.toString())
        return binding.root
    }



    private fun clickListener() {
        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.quillViewer.settings.javaScriptEnabled = true
        binding.quillViewer.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.quillViewer.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
                }

                return super.onConsoleMessage(consoleMessage)
            }


        }

        binding.quillViewer.addJavascriptInterface(WebAppInterface(), "Android")
        binding.quillViewer.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }
        }

        binding.quillViewer.loadUrl("file:///android_asset/quill.html")
        binding.sendButton.setOnClickListener {
            // Execute JavaScript code to trigger the click event on the sendButton
            binding.quillViewer.evaluateJavascript("document.getElementById('sendButton').click();") { result ->


            }

        }

    }


    private fun PostsApi(message: String) {
        var tvselector = valueIdentifier
        chatMessage = CommonUtils.stripHtml(message)

        if (tvselector.isEmpty()) {
            CommonUtils.toast(requireActivity(),"Please select a category")
            // Show error message for empty email

        }else if(chatMessage.isEmpty()){
           CommonUtils.toast(requireActivity(),"Please type a message")
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
            formData.put("post_category_identifier", tvselector) //email or phone
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
                ViewModalClass().CreatePostLiveData(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer "+it,
                    contentKey
                ).observe(requireActivity()) { createPostResponse ->
                    createPostResponse?.let { response ->
                        if (response.statusCode == 201) {
                            CommonUtils.toast(requireActivity(), response.message ?: "Post Created Successfully")
                            tvselector=""
                            categoryMessage=""


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




    inner class WebAppInterface {

        @JavascriptInterface
        fun getTextFromTextArea(content: String) {
            // Handle the received text here
            Log.d("WebView", "Text from textarea: $content")
            // You can perform additional actions with the received content, such as displaying it in a Toast
           // Toast.makeText(requireActivity(), content, Toast.LENGTH_LONG).show()
             categoryMessage=content

            PostsApi(categoryMessage)
        }

        @JavascriptInterface
        fun setQuillContent(htmlContent: String) {
            Log.d("WebView2222", "Text from textarea: $htmlContent")


        }
    }

    private fun getCategories(
        requireActivity: FragmentActivity) {

        ViewModalClass().getCategoriesLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { categoriesResponse: CategoriesResponse? ->
            categoriesResponse?.let { nonNullcategoriesResponse ->
                if (categoriesResponse.statusCode == 200) {

                    if(categoriesResponse.data != null) {
                        for (i in 0 until categoriesResponse!!.data!!.records.size) {
                            arrayList?.add(categoriesResponse.data!!.records[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(requireActivity, "Failed to retrieve testScore. Please try again.");
                    }

                    setDropDownCategories()

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        categoriesResponse.message ?: " Failed"
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDropDownCategories() {
        binding?.tvAcpCountrySelector?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Category")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvAcpCountrySelector.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                binding?.tvAcpCountrySelector?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvAcpCountrySelector?.height!!
            } else {
                // If not in a fragment, use activity view for positioning
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvAcpCountrySelector?.height!!
            }


            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterCategoriesSelector(requireActivity(), arrayList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCampus ->
                binding?.tvAcpCountrySelector?.text = selectedCampus.name
                valueIdentifier=selectedCampus.identifier

                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }
// Show the popup window
            popupWindow.showAsDropDown(binding?.tvAcpCountrySelector)


            //search edit text
            layout.requireViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val enteredText = s.toString()
                        adapter.getFilter().filter(enteredText)
                    }
                })
        }
    }
    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }

}