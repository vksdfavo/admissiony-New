package com.student.Compass_Abroad.Scout.fragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.FragmentScoutMessageBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragmentScoutMessage : BaseFragment() {
   private lateinit var binding:FragmentScoutMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentScoutMessageBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        setUpWebView()


        return binding.root
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

    inner class WebAppInterface {
        @JavascriptInterface
        fun getTextFromTextArea(content: String) {

            val title = binding.etTitle.text.toString()

        }

        @JavascriptInterface
        fun setQuillContent(htmlContent: String) {
            Log.d("WebView2222", "Text from textarea: $htmlContent")
        }
    }

}