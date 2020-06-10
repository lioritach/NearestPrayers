package com.example.group21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group21.Authentication.GabayLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainGabayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gabayName, gabaySynName, gabayEmail;
    private ImageButton logoutGabay_btn, editProfileGabay, addSynGabay;
    private ImageView gabayProfile;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CardView book, zmani_hayum, minyan, halacha_yomit, showMySynagogues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gabay);

        gabayName = (TextView) findViewById(R.id.nameGabbayActivityID);
        gabaySynName = (TextView) findViewById(R.id.nameOfSynGabayID);
        gabayEmail = (TextView) findViewById(R.id.EmailGabayID);
        logoutGabay_btn = (ImageButton) findViewById(R.id.logoutGabbayID);
        editProfileGabay = (ImageButton) findViewById(R.id.editProfileGabayID);
        addSynGabay = (ImageButton) findViewById(R.id.addSynagogueGabay);
        gabayProfile = (ImageView) findViewById(R.id.profileGabay);

        book = (CardView) findViewById(R.id.siddur_cardID);
        book.setOnClickListener(this);

        zmani_hayum = (CardView) findViewById(R.id.zmaniHayum_cardID);
        zmani_hayum.setOnClickListener(this);


        minyan = (CardView) findViewById(R.id.findMinyan_cardID);
        minyan.setOnClickListener(this);

        halacha_yomit = (CardView) findViewById(R.id.halacha_yomit_cardID);
        halacha_yomit.setOnClickListener(this);

        showMySynagogues = (CardView) findViewById(R.id.showMySynagogues_CardID);
        showMySynagogues.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן ...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        logoutGabay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline, sign out, go to login activity
                makeMeOffline();
            }
        });

        editProfileGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile activity
                startActivity(new Intent(MainGabayActivity.this, GabayEditProfile.class));
            }
        });

        addSynGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainGabayActivity.this, AddSynagogue.class));
            }
        });
    }

    private void makeMeOffline() {
        //after logging in make user online
        progressDialog.setMessage("מתנתק מהמערכת ...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update successfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(MainGabayActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainGabayActivity.this, GabayLogin.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //get data from db
                            String name = "" + ds.child("name").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String email = "" + ds.child("email").getValue();
                            String synName = "" + ds.child("synagogueName").getValue();
                            String profileImageGabay = "" + ds.child("profileImage").getValue();

                            //set data to UI
                            gabayName.setText(name);
                            gabayEmail.setText(email);
                            gabaySynName.setText(accountType);

                            try {
                                Picasso.get().load(profileImageGabay).placeholder(R.drawable.icons8_synagogue_40).into(gabayProfile);
                            } catch (Exception e) {
                                gabayProfile.setImageResource(R.drawable.icons8_synagogue_40);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


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
            case R.id.showMySynagogues_CardID:
                intent = new Intent(this, ShowMySynagogues.class);
                startActivity(intent);
                break;
        }
    }


}
