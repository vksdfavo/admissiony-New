package com.student.Compass_Abroad.activities

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityDownLoadDocBinding
import com.student.bt_global.Utils.NeTWorkChange

class DownLoadDocActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDownLoadDocBinding
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    companion object {
        lateinit var data: String
        lateinit var extension: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownLoadDocBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(android.R.color.white)
        }

        // Set navigation bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(android.R.color.black)
        }

        // Make sure the status bar and navigation bar icons are dark (to be visible on white background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }


        if (data.isNotEmpty()) {

            setupWebView(data, extension)
        }

    }
    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
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
            Toast.makeText(this, "Invalid URL for download", Toast.LENGTH_SHORT).show()
            return
        }

        android.util.Log.d("WebViewFragment", "Download URL: $url")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Image")
            .setDescription("Downloading image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_image.$fileExtension")

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(this, "Download Started", Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(neTWorkChange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(neTWorkChange)
        super.onStop()
    }
}