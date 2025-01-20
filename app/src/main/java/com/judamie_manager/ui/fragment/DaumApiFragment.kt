package com.judamie_manager.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName

class DaumApiFragment : Fragment() {

    companion object {
        const val ADDRESS_REQUEST_CODE = 2928
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daum_api, container, false)
        val webView = view.findViewById<WebView>(R.id.web_view)

        webView.settings.javaScriptEnabled = true

        webView.addJavascriptInterface(KaKaoJavaScriptInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:execKakaoPostcode();")
            }
        }

        // webView.loadUrl("http://hyemimkim.github.io/webHostiongTest/indexx.html")
        webView.loadUrl("http://hyemimkim.github.io/webhosting3/index2.html")

        return view
    }

    inner class KaKaoJavaScriptInterface {

        @JavascriptInterface
        fun processDATA(address: String?) {
            // FragmentResultListener를 통해 주소를 전달
            parentFragmentManager.setFragmentResult("addressRequest", Bundle().apply {
                putString("address", address)
            })

            // 프래그먼트를 pop하여 돌아가기
            parentFragmentManager.popBackStack()
        }
    }
}