package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class Ashkenaz extends AppCompatActivity {

    @Override
    //Open "ashkenazi" sidur
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ashkenaz);

        WebView webView = new WebView(this);
        setContentView(webView);

        //source for siddur ashkenazi
        webView.loadUrl("https://www.sefaria.org.il/Siddur_Ashkenaz%2C_Weekday%2C_Shacharit%2C_Preparatory_Prayers%2C_Tallit?lang=he");

    }


}

