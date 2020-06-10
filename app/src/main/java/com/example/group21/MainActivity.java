package com.example.group21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.group21.Authentication.GabayLogin;
import com.example.group21.Payment.Payment;
import com.example.group21.slides.OnBoarding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //for the toolbar and navigationView
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private CardView book, zmani_hayum, minyan, halacha_yomit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book = (CardView) findViewById(R.id.siddur_cardID);
        book.setOnClickListener(this);

        zmani_hayum = (CardView) findViewById(R.id.zmaniHayum_cardID);
        zmani_hayum.setOnClickListener(this);

//        syn = (CardView) findViewById(R.id.synagogue_cardID);
//        syn.setOnClickListener(this);

        minyan = (CardView) findViewById(R.id.findMinyan_cardID);
        minyan.setOnClickListener(this);

        halacha_yomit = (CardView) findViewById(R.id.halacha_yomit_cardID);
        halacha_yomit.setOnClickListener(this);

        /*--------Hooks---------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*--------Tool bar---------*/
        setSupportActionBar(toolbar);


        /*--------Navigation Drawer Menu ---------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }


    /**
     * implement the toolbar options
     * @param menuItem
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_login:
                Intent nav_login_intent = new Intent(MainActivity.this, GabayLogin.class);
                startActivity(nav_login_intent);

                break;
//            case R.id.nav_logout:
//                Intent nav_logout_intent = new Intent(MainActivity.this, Login.class);
//                FirebaseAuth.getInstance().signOut();
//                startActivity(nav_logout_intent);
//                finish();
//                break;
            case R.id.nav_payment:
                Intent nav_payment_intent = new Intent(MainActivity.this, Payment.class);
                startActivity(nav_payment_intent);
                break;
            case R.id.nav_contactUs:
                Intent nav_contactUs_intent = new Intent(MainActivity.this, ContactUs.class);
                startActivity(nav_contactUs_intent);
                break;
            case R.id.nav_rate:
                Intent nav_rateUs_intent = new Intent(MainActivity.this, RateUs.class);
                startActivity(nav_rateUs_intent);
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSub = "אפליקציית NearestPrayers";
                String shareBody = "היי, רציתי להמליץ לך על אפליקצית מציאת מניינים הקרובים אלייך, חפש עכשיו 'יגעת ומצאת' ב Google Play :)";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "שתף באמצעות"));
                break;
            case R.id.nav_help:
                Intent nav_help_intent = new Intent(MainActivity.this, OnBoarding.class);
                startActivity(nav_help_intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
        }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    /**
     * implement the icons in the home page
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.siddur_cardID:
                intent = new Intent(this, Siddur.class);
                startActivity(intent);
                break;
            case R.id.zmaniHayum_cardID:
                intent = new Intent(this, ZmaniHayum.class);
                startActivity(intent);
                break;
            case R.id.findMinyan_cardID:
                intent = new Intent(this, map.class);
                startActivity(intent);
                break;
            case R.id.halacha_yomit_cardID:
                intent = new Intent(this, HalachaYomit.class);
                startActivity(intent);
                break;
        }

    }


}
