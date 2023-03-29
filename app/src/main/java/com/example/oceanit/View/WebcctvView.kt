package com.example.oceanit.View

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.oceanit.R

class WebcctvView : AppCompatActivity() {

    private var webView : WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv_view)

        webView = findViewById(R.id.webView)
        val webSettings = webView!!.settings

        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 로그인 페이지 URL
                if (url == "http://211.184.227.81:8500/login") {
                    view.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByName('id')[0].value='user1';" +
                                "document.getElementsByName('pw')[0].value='user1';" +
                                "document.forms[0].submit();" +
                                "})()"
                    )
                } else if (url == "http://211.184.227.81:8500/cctvView") {

                }
            }
        }

//        webView.webViewClient = WebViewClient() //새창 열기 없이 웹뷰내에서 다시 열기
        webSettings.loadWithOverviewMode = true
        webSettings.javaScriptEnabled = true // allow the js
        webSettings.useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(true) // 화면 줌 허용 여부
        webSettings.builtInZoomControls = true // 화면 확대 축소 허용 여부
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
        webSettings.domStorageEnabled = true


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //화면이 계속 켜짐

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

        webView!!.loadUrl("http://211.184.227.81:8500/cctvView")
    }
}