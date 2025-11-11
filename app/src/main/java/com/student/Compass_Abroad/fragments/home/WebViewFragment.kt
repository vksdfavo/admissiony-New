package com.student.Compass_Abroad.fragments.home

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.student.Compass_Abroad.databinding.FragmentWebViewBinding
import android.widget.Toast
import com.student.Compass_Abroad.fragments.BaseFragment

class WebViewFragment : BaseFragment() {

    private lateinit var binding: FragmentWebViewBinding

    companion object {
        lateinit var data: String
        lateinit var extension: String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)

        if (data.isNotEmpty()) {
            setupWebView(data, extension)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =  requireActivity().getColor(android.R.color.white)
        }

        // Set navigation bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.navigationBarColor =  requireActivity().getColor(android.R.color.black)
        }

        // Make sure the status bar and navigation bar icons are dark (to be visible on white background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
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

                // Directly pass the data URL instead of the one in onPageFinished
                if (!fileExtension.equals("pdf", ignoreCase = true)) {
                    downloadImage(data, fileExtension)
                }
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


    private fun downloadImage(url: String?, fileExtension: String) {
        if (url.isNullOrEmpty() || url == "about:blank") {
            Toast.makeText(requireContext(), "Invalid URL for download", Toast.LENGTH_SHORT).show()
            return
        }

        // Log the URL to see what is being passed
        android.util.Log.d("WebViewFragment", "Download URL: $url")

        // Generate a unique filename with the current time in milliseconds
        val timestamp = System.currentTimeMillis()
        val filename = "downloaded_image_$timestamp.$fileExtension"

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Image")
            .setDescription("Downloading image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(requireContext(), "Download Started", Toast.LENGTH_SHORT).show()
    }


}
