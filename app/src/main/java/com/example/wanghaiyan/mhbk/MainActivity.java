package com.example.wanghaiyan.mhbk;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public String code = "";
    public String custNo = "";
    private WebView wb_mhbk;
    private boolean flg = false;
    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        wb_mhbk = findViewById(R.id.wb_mhbk);
        wb_mhbk.loadUrl(url);
        WebSettings settings = wb_mhbk.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setUseWideViewPort(true);
//        settings.setSupportZoom(true);
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wb_mhbk.setWebViewClient(new WebViewClient(){
            String code = "";
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                Uri uri = Uri.parse(url);
                if(uri.getQueryParameter("code") != null){
                    code = uri.getQueryParameter("code");
                    String cusno = "お客さま番号";
                    String strCode = "Code";
                    String mes = cusno + ":" + custNo + "\n" + strCode + ":" + code;
                    Toast.makeText(MainActivity.this,mes,Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.java_obj.getPwdSource(document.getElementById('PASSWD_LoginPwdInput').innerText);void(0)");
                if(flg){
                    view.loadUrl("javascript:window.java_obj.getNoSource(document.getElementById('txtCustNo').innerText);void(0)");
                }
                super.onPageFinished(view, url);

            }
        });
        wb_mhbk.setWebChromeClient(new WebChromeClient());
        wb_mhbk.requestFocusFromTouch();
        wb_mhbk.addJavascriptInterface(new InJavaScriptLocalObj(),"java_obj");
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getNoSource(String htmlNoData) {
            if(flg){
                custNo = htmlNoData;
                Toast.makeText(MainActivity.this,custNo, Toast.LENGTH_SHORT).show();
            }
        }
        @JavascriptInterface
        public void getPwdSource(String htmlPwdData) {
            if(htmlPwdData!=null){
                flg = true;
            }
        }
    }

}


