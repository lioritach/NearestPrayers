package com.example.prayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class zmaniHayom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmany_hayom);

        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl("");
    }
}
