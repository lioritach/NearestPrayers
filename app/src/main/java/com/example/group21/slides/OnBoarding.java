package com.example.group21.slides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.group21.HelperClasses.SliderAdapter;
import com.example.group21.MainActivity;
import com.example.group21.R;

public class OnBoarding extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;
    private Button back_to_app;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        //Hooks
        viewPager = findViewById(R.id.sliderID);
        dotsLayout = findViewById(R.id.dotsID);
        back_to_app = findViewById(R.id.back_to_app_btnID);

        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    //Implement the skip button
    public void skip(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    //Implement the next button
    public void next(View view){
        viewPager.setCurrentItem(currentPosition + 1);
    }

    //Implement the back to home page button
    public void backHomePage(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    //Add the dots to the screen
    private void addDots(int pos){

        dots = new TextView[5];
        dotsLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0 ){
            dots[pos].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    //when page is scrolled
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            currentPosition = position;

            if(position == 0){
                back_to_app.setVisibility(View.INVISIBLE);
            }
            else if(position == 1){
                back_to_app.setVisibility(View.INVISIBLE);
            }
            else if(position == 2){
                back_to_app.setVisibility(View.INVISIBLE);

            }
            else if(position == 3){
                back_to_app.setVisibility(View.INVISIBLE);
            }
            else{
                back_to_app.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
