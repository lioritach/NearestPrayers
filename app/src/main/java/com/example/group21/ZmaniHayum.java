package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ZmaniHayum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmani_hayum);

        WebView webView = new WebView(this);
        setContentView(webView);

        //zmanei hayom source
        webView.loadUrl("https://calendar.2net.co.il/todaytimes.aspx");
    }
}