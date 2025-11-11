package com.student.Compass_Abroad.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentWebViewButtonBinding


class WebViewButton : BaseFragment() {
    var  binding: FragmentWebViewButtonBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWebViewButtonBinding.inflate(inflater, container, false)


        binding!!.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        // Inflate the layout for this fragment

        binding?.webview?.settings?.javaScriptEnabled = true
        binding?.webview?.settings?.domStorageEnabled = true


        val htmlContent = """
<html>
<head>
<title>Accomudations</title>
</head>
<body>
<div id="casita-widget">
    <div id="filters"></div>
    <div id="loading" class="loading">Loading...</div>
    <div id="content"></div>
</div>
<div class="casita-link" style="text-align: center">
    <a href="https://casita.com?utm_source=admissiony.com&partner_code=32300" target="_blank" rel="noopener" class="powered">Powered by <span>casita.com</span></a>
</div>
<script>
    ;(function (w, d, s, o, f, js, fjs) {
      w[o] =
        w[o] ||
        function () {
          ;(w[o].q = w[o].q || []).push(arguments)
        }
      ;(js = d.createElement(s)), (fjs = d.getElementsByTagName(s)[0])
      js.id = o
      js.src = f
      js.async = 1
      fjs.parentNode.insertBefore(js, fjs)
    })(window, document, 'script', '_aw', 'https://www.casita.com/widget/Accommodation-Widget/minify-widget.js')
    _aw('init', {
      element: document.getElementById('content'),
      country:'uk', // required
      location: 'london', // required
      partnerCode: '32300',
      utmSource:'admissiony.com'
    })
</script>
</body>
</html>
""".trimIndent()

        binding?.webview?.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)



        return binding?.root


    }


    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav?.visibility = View.GONE
    }


}