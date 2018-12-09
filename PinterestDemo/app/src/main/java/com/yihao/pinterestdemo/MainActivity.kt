package com.yihao.pinterestdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {

    private var _webView: WebView? = null

    companion object {
        const val TAG: String = "Pinterest"
        private var CONNECTION_TIME_OUT = 30 * 1000
        private var READ_TIME_OUT = 30 * 1000
        private var WRITE_TIME_OUT = 30 * 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _webView = findViewById(R.id.webView)
        _webView?.loadUrl("https://api.pinterest.com/oauth/?response_type=code&redirect_uri=https://com.yihao.test/pinterest/&client_id=5003910933785893619&scope=read_public,write_public&state=768uyFys")
        _webView?.settings?.javaScriptEnabled = true
        _webView?.webViewClient = object : WebViewClient() {
            // 此处可以用RxJava优化
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.v(TAG, url)
                if (url!!.startsWith("https://com.yihao.test")) {
                    var index = url.indexOf("code=")
                    var code = url.substring(index + 5, url.length)
                    Log.v(TAG, "code:$code")
                    var intent = Intent()
                    intent.putExtra("code", code)
                    intent.setClass(this@MainActivity, UserActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
