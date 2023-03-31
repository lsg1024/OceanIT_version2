package com.example.oceanit.View

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.example.oceanit.DB.AppDatabase
import com.example.oceanit.R

class WebcctvView : AppCompatActivity() {

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv_view)

        webView = findViewById(R.id.webView)
        val webSettings = webView!!.settings

        val db = AppDatabase.getDBInstance(this.applicationContext)

        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 로그인 페이지 URL
                if (url == "http://211.184.227.81:8500/login") {
                    view.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByName('id')[0].value='" + "${db!!.UserDao()!!.getUser(0).loginId}';" +
                                "document.getElementsByName('pw')[0].value='${db.UserDao()!!.getUser(0).loginPw}';" +
                                "document.forms[0].submit();" +
                                "})()"
                    )
                } else if (url == "http://211.184.227.81:8500/cctvView") {
                    // 로그인 후 CCTV 화면으로 이동
                }
            }
        }

        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                // 자동 로그인 후 페이지 이동 처리
                webView!!.loadUrl("http://211.184.227.81:8500/cctvView")
                result.confirm()
                return true
            }
        }

        webSettings.loadWithOverviewMode = true
        webSettings.javaScriptEnabled = true // allow the js
        webSettings.useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(false) // 화면 줌 허용 여부
        webSettings.builtInZoomControls = false // 화면 확대 축소 허용 여부
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
        webSettings.domStorageEnabled = true

        webView!!.loadUrl("http://211.184.227.81:8500/cctvView")
    }
}
