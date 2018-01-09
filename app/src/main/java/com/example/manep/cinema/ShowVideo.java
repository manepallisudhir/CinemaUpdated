package com.example.manep.cinema;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by manep on 9/8/2017.
 */

public class ShowVideo extends AppCompatActivity {
    URL url;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        String videourl = getIntent().getStringExtra("URL");
        /*try {
            url = new URL(videourl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        Toast.makeText(this,"Wait till loading",Toast.LENGTH_LONG).show();
        WebView webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.getSettings().getAllowContentAccess();
        //WebSettings webSettings = webView.getSettings();
       // webSettings.setUseWideViewPort(true);
      //  webSettings.setLoadsImagesAutomatically(true);
       // webSettings.setLoadWithOverviewMode(true);
       // webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videourl);

        /*web.getSettings().setAllowContentAccess(true);
        WebSettings webSettings = web.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        web.canGoBack();
        web.setWebChromeClient(new WebChromeClient() {});*/

    }
}
