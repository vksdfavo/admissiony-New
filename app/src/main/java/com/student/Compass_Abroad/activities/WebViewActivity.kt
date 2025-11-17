package com.student.Compass_Abroad.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private var hasError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val url = intent.getStringExtra("url") ?: intent.getStringExtra("view_page")
        val extension = intent.getStringExtra("extension")?.lowercase()
            ?: intent.getStringExtra("file_extension")?.lowercase()

        // Validate inputs
        if (url.isNullOrEmpty() || extension.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid file URL", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.backBtn.setOnClickListener {

            onBackPressed()
        }

        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        setupWebView(url, extension)
    }

    private fun setupWebView(url: String, extension: String) {
        // Show progress bar initially
        binding.progressBar.visibility = View.VISIBLE
        binding.webView.visibility = View.VISIBLE

        // Configure WebView settings
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
                allowFileAccess = true
                allowContentAccess = true

                // Enable caching for better performance
                cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (!hasError) {
                        binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    hasError = true
                    binding.progressBar.visibility = View.GONE

                    // Only show error for main frame failures
                    if (request?.isForMainFrame == true) {
                        Toast.makeText(
                            this@WebViewActivity,
                            "Failed to load file. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    // Allow all URLs to load in WebView
                    return false
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    // Update progress bar if needed
                    if (newProgress == 100) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        // Load content based on file type
        when (extension) {
            "pdf" -> {
                loadPdfFile(url)
            }
            "doc", "docx", "xls", "xlsx", "csv" -> {
                loadOfficeDocument(url)
            }
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg" -> {
                loadImageFile(url)
            }
            else -> {
                // Try external app first, fallback to viewer
                openInExternalApp(url, extension)
            }
        }
    }

    /**
     * Load PDF using Google Docs Viewer
     */
    private fun loadPdfFile(url: String) {
        val viewerUrl = "https://docs.google.com/viewer?embedded=true&url=${Uri.encode(url)}"
        binding.webView.loadUrl(viewerUrl)
    }

    /**
     * Load Office documents using Microsoft Office Online Viewer
     */
    private fun loadOfficeDocument(url: String) {
        val viewerUrl = "https://view.officeapps.live.com/op/view.aspx?src=${Uri.encode(url)}"
        binding.webView.loadUrl(viewerUrl)
    }

    /**
     * Load image directly in HTML with proper styling
     */
    private fun loadImageFile(url: String) {
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes">
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    body {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        background-color: #f5f5f5;
                        padding: 10px;
                    }
                    img {
                        max-width: 100%;
                        max-height: 100vh;
                        width: auto;
                        height: auto;
                        object-fit: contain;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    }
                </style>
            </head>
            <body>
                <img src="$url" alt="Image" onerror="this.style.display='none'; document.body.innerHTML='<p style=color:red;text-align:center;>Failed to load image</p>'"/>
            </body>
            </html>
        """.trimIndent()

        binding.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

    /**
     * Try to open file in external app
     */
    private fun openInExternalApp(url: String, extension: String) {
        try {
            val mimeType = getMimeType(extension)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(url), mimeType)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                finish()
            } else {
                // Fallback to appropriate viewer
                fallbackToViewer(url, extension)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fallbackToViewer(url, extension)
        }
    }

    /**
     * Get MIME type for file extension
     */
    private fun getMimeType(extension: String): String {
        return when (extension.lowercase()) {
            "csv" -> "text/csv"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            "webp" -> "image/webp"
            "svg" -> "image/svg+xml"
            else -> "*/*"
        }
    }

    /**
     * Fallback to web viewer based on file type
     */
    private fun fallbackToViewer(url: String, extension: String) {
        binding.webView.visibility = View.VISIBLE

        when (extension.lowercase()) {
            "pdf" -> loadPdfFile(url)
            in listOf("doc", "docx", "xls", "xlsx", "csv") -> loadOfficeDocument(url)
            in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg") -> loadImageFile(url)
            else -> {
                // Try Office viewer as last resort
                loadOfficeDocument(url)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Set light status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Resume WebView
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        // Pause WebView to save resources
        binding.webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up WebView
        binding.webView.apply {
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            destroy()
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}