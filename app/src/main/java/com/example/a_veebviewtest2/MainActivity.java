package com.example.a_veebviewtest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
    private Button buttonMe;
    private Button buttonApp;
    private Button buttonMap;
    private Button buttonPosition;
    Button.OnClickListener control = new View.OnClickListener () {
        public void onClick(View view) {
            Intent intent;
            switch (view.getId ()) {
                case R.id.me://数据采集
                    intent = new Intent (MainActivity.this, position.class);
                    startActivity (intent);
                    break;
                case R.id.app://平面
                    intent = new Intent (MainActivity.this, appActivity.class);
                    startActivity (intent);
                    break;
                case R.id.map://3D
//                    intent = new Intent (MainActivity.this, mapActivity.class);
//                    startActivity (intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i ("1","MainActivity");

        webView = (WebView) findViewById (R.id.webView);
        WebSettings webSettings = webView.getSettings ();
        //支持JS
        webSettings.setJavaScriptEnabled (true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setUserAgentString (userAgent); //设置userAgent为电脑端的ua
        webSettings.setCacheMode (WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled (true);
        webSettings.setMediaPlaybackRequiresUserGesture (false);
        webSettings.setMixedContentMode (WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager.getInstance ().setAcceptThirdPartyCookies (webView, true);
        //支持屏幕缩放
        webSettings.setSupportZoom (true);
        webSettings.setBuiltInZoomControls (true);

        webView.loadUrl ("http://2k75g60137.qicp.vip/WeiXinDemo/index.html");
        //http://25f433f494.zicp.vip/WeiXinDemo/index.html

        //加上这个句，不然直接使用浏览器打开
        webView.setWebViewClient (new WebViewClient () {
            /**
             * 防止断网时的错误页面
             */
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError (webView, i, s, s1);
                webView.loadUrl ("about:blank");
            }

            /**
             * 允许非验证的ssl证书，防止http出错
             */
            @Override
            public void onReceivedSslError(WebView webView1, SslErrorHandler handler, SslError error) {
                handler.proceed ();
                super.onReceivedSslError (webView, handler, error);
            }
        });
        buttonMe = (Button) findViewById (R.id.me);
        buttonMe.setOnClickListener (control);
        buttonApp = (Button) findViewById (R.id.app);
        buttonApp.setOnClickListener (control);
        buttonMap = (Button) findViewById (R.id.map);
        buttonMap.setOnClickListener (control);
    }
}
