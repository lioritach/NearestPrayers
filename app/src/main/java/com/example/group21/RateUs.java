package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateUs extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView rateUs_text;
    private ImageView backrateUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar_ID);
        rateUs_text = (TextView) findViewById(R.id.rateUs_txtID);
        backrateUs = (ImageView) findViewById(R.id.backrateUsID);

        ratingBar.setRating(load());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateUs_text.setText("" + rating);
                save(rating);
            }
        });

        backrateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void save(float f){
        SharedPreferences sharedPreferences = getSharedPreferences("folder", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("rating", f);
        editor.commit();
    }

    public float load(){
        SharedPreferences sharedPreferences = getSharedPreferences("folder", MODE_PRIVATE);
        float f = sharedPreferences.getFloat("rating", 0f);
        return f;
    }
}
