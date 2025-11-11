package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterChatsTabs
import com.student.Compass_Abroad.databinding.FragmentConversationBinding
import com.student.Compass_Abroad.fragments.BaseFragment

class FragmentConversation : BaseFragment() {
    private lateinit var binding: FragmentConversationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentConversationBinding.inflate(inflater, container, false)


        onTabCLickListener()
        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.fabFadBack.setOnClickListener { v: View ->

            requireActivity().onBackPressed()
        }
    }


    private fun onTabCLickListener() {
        binding.tlFh.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpTa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //view pager
        binding.vpTa.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tlFh.selectTab(binding.tlFh.getTabAt(position))
            }
        })

        //click listeners

    }

    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter =
            AdapterChatsTabs(this, binding.tlFh.tabCount)
        binding.vpTa.setOffscreenPageLimit(2)
        binding.vpTa.setAdapter(adapter)
//        val index = binding.vpTa.currentItem
//        val frag = adapter.list[index]
//        frag.onResume()

    }

    /* private fun setRecyclerView() {

         adapterApplicationDetailConversation = AdapterApplicationDetailConversation(context)
             val layoutManager =
                 LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
             binding!!.rvMessages.setLayoutManager(layoutManager)
             binding!!.rvMessages.setAdapter(adapterApplicationDetailConversation)

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
         binding.fabSend.setOnClickListener {
             // Execute JavaScript code to trigger the click event on the sendButton
             binding.quillViewer.evaluateJavascript("document.getElementById('sendButton').click();") { result ->


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
             //categoryMessage=content

             //PostsApi(categoryMessage)
         }

         @JavascriptInterface
         fun setQuillContent(htmlContent: String) {
             Log.d("WebView2222", "Text from textarea: $htmlContent")


         }
     }*/


    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible = false
        Log.i("TAG", "onResume: dvv")
        setTabAdaptor()
    }
}