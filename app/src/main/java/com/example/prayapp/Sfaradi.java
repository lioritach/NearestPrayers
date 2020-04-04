package com.example.prayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Sfaradi extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfaradi);

        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl("https://www.sefaria.org.il/Siddur_Edot_HaMizrach%2C_Weekday_Shacharit%2C_Morning_Prayer?lang=he");
    }
}
