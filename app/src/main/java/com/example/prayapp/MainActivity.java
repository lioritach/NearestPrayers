package com.example.prayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView book, synagogue, zmanyHayom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book = (CardView) findViewById(R.id.siddur_cardID);
        book.setOnClickListener(this);
        synagogue = (CardView) findViewById(R.id.synagogue_cardID);
        synagogue.setOnClickListener(this);
        zmanyHayom = (CardView) findViewById(R.id.zmaniHayum_cardID);
        zmanyHayom.setOnClickListener(this);


        /*--------Hooks---------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*--------Tool bar---------*/
        setSupportActionBar(toolbar);

        /*--------Navigation Drawer Menu ---------*/

        //Hide or show items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_add).setVisible(false);
        menu.findItem(R.id.nav_event).setVisible(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
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

    /* The icons in the navigation bar */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_help:
                Intent nav_help_intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(nav_help_intent);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_login:
                Intent nav_login_intent = new Intent(MainActivity.this, Login.class);
                startActivity(nav_login_intent);
                break;
            case R.id.nav_logout:
                Intent nav_logout_intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(nav_logout_intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.siddur_cardID:
                intent = new Intent(this, Siddur.class);
                startActivity(intent);
                break;
            case R.id.synagogue_cardID:
                intent = new Intent(this, Synagogue.class);
                startActivity(intent);
                break;
            case R.id.zmaniHayum_cardID:
                intent = new Intent(this, zmaniHayom.class);
                startActivity(intent);
                break;
        }
    }
}
