package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class Siddur extends AppCompatActivity {

    private TextView ashkenaz_view, sfaradi_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siddur);

        ashkenaz_view = findViewById(R.id.ashkenaz);
        sfaradi_view = findViewById(R.id.sfaradi);


        //sfaradi and ashkenazi butttons
        ashkenaz_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add Button for Ashkenaz
                startActivity(new Intent(getApplicationContext(),Ashkenaz.class));

            }
        });

        sfaradi_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add Button for Sfaradi
                startActivity(new Intent(getApplicationContext(),Sfaradi.class));

            }
        });

    }
}
