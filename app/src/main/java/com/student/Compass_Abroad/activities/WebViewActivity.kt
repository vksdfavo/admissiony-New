package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityWebViewBinding
import com.student.bt_global.Utils.NeTWorkChange

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebViewBinding
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val url = intent.getStringExtra("view_page") ?: ""
        val fileExtension = intent.getStringExtra("file_extension") ?: ""

        if (url.isNotEmpty()) {
            setupWebView(url, fileExtension)
        }
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