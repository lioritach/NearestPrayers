package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class HalachaYomit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halacha_yomit);
        
        //Open referenced web-site//
        WebView webView = new WebView(this);
        setContentView(webView);

        //source for halacha yomit
        webView.loadUrl("http://halachayomit.co.il/");

    }
}
