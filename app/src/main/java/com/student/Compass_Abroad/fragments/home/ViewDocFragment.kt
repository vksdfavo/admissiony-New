package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.student.Compass_Abroad.databinding.FragmentViewDocBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class ViewDocFragment : BaseFragment() {
    private lateinit var binding: FragmentViewDocBinding


    companion object {

        lateinit var data: String
        lateinit var extension: String


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding = FragmentViewDocBinding.inflate(inflater, container, false)

        if (data.isNotEmpty()) {
            setupWebView(data, extension)
        }

        return binding.root
    }

    private fun setupWebView(url: String, fileExtension: String) {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                binding.progressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }

        binding.webView.settings.defaultZoom = WebSettings.ZoomDensity.FAR
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.javaScriptEnabled = true

        if (fileExtension.equals("pdf", ignoreCase = true)) {
            val pdfViewerUrl = "https://docs.google.com/viewer?url=$url"
            binding.webView.loadUrl(pdfViewerUrl)
        } else {
            val htmlData = """
                <html>
                <head>
                    <style>
                        body {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                        }
                        img {
                            max-width: 100%;
                            max-height: 100%;
                            object-fit: contain;
                        }
                    </style>
                </head>
                <body>
                    <img src="$url" />
                </body>
                </html>
            """
            binding.webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
        }
    }
}