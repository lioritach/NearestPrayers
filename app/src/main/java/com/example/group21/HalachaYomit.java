package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class HalachaYomit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halacha_yomit);
        
        //open web//
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl("http://halachayomit.co.il/");

    }
}