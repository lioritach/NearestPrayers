package com.example.group21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;


public class ShowMySynagogues extends AppCompatActivity {

    private TextView gabayName, gabaySynName, gabayEmail, gabaySynagogues_TAB, filteredSynagogue;
    private EditText searchSynagogue;
    private ImageButton logoutGabay_btn, editProfileGabay, addSynGabay, filterSynagogue;
    private ImageView gabayProfile;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RelativeLayout synagoguesRL;
    private RecyclerView synagoguesRecyclerView;
    private ArrayList<ModelShowSynagogues> synagoguesList;
    private AdapterSynagogues adapterSynagogues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_synagogues);

        gabayName = (TextView) findViewById(R.id.nameGabbayActivityID);
        gabaySynName = (TextView) findViewById(R.id.nameOfSynGabayID);
        gabayEmail = (TextView) findViewById(R.id.EmailGabayID);
        gabaySynagogues_TAB = (TextView) findViewById(R.id.mySynTab_ID);
        filteredSynagogue = (TextView) findViewById(R.id.filteredSynagogueID);
        searchSynagogue = (EditText) findViewById(R.id.searchSynagogueID);
        logoutGabay_btn = (ImageButton) findViewById(R.id.logoutGabbayID);
        editProfileGabay = (ImageButton) findViewById(R.id.editProfileGabayID);
        addSynGabay = (ImageButton) findViewById(R.id.addSynagogueGabay);
        filterSynagogue = (ImageButton) findViewById(R.id.filterSynagogueID);
        gabayProfile = (ImageView) findViewById(R.id.profileGabay);
        synagoguesRL = (RelativeLayout) findViewById(R.id.mySynRelativeLayout);
        synagoguesRecyclerView = (RecyclerView) findViewById(R.id.synagoguesRecyclerViewID);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן ...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllSynagogues();
        showMySynagoguesUI();

        //serach
        searchSynagogue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterSynagogues.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                startActivity(new Intent(ShowMySynagogues.this, GabayEditProfile.class));
            }
        });

        addSynGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowMySynagogues.this, AddSynagogue.class));
            }
        });

        gabaySynagogues_TAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load synagogues
                showMySynagoguesUI();
            }
        });

        filterSynagogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowMySynagogues.this);
                builder.setTitle("בחר קטגוריה")
                        .setItems(Constants.options1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.options1[which];
                                filteredSynagogue.setText(selected);
                                if(selected.equals("הכל")){
                                    //load all
                                    loadAllSynagogues();
                                }
                                else{
                                    //load filtered
                                    loadFilteredSynagogues(selected);
                                }
                            }
                        })
                        .show();
            }
        });

    }

    private void loadFilteredSynagogues(String selected) {
        synagoguesList = new ArrayList<>();

        //get all the synagogues
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("SynagogueDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        synagoguesList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            String synCategories = ""+ds.child("category").getValue();
                            //if selected category matches product category, then add in list
                            if(selected.equals(synCategories)){
                                ModelShowSynagogues modelShowSynagogues = ds.getValue(ModelShowSynagogues.class);
                                synagoguesList.add(modelShowSynagogues);
                            }

                        }
                        //setup adapter
                        adapterSynagogues = new AdapterSynagogues(ShowMySynagogues.this, synagoguesList);
                        //set adapter
                        synagoguesRecyclerView.setAdapter(adapterSynagogues);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadAllSynagogues() {
        synagoguesList = new ArrayList<>();

        //get all the synagogues
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("SynagogueDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        synagoguesList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            ModelShowSynagogues modelShowSynagogues = ds.getValue(ModelShowSynagogues.class);
                            synagoguesList.add(modelShowSynagogues);
                        }
                        //setup adapter
                        adapterSynagogues = new AdapterSynagogues(ShowMySynagogues.this, synagoguesList);
                        //set adapter
                        synagoguesRecyclerView.setAdapter(adapterSynagogues);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void showMySynagoguesUI() {
        synagoguesRL.setVisibility(View.VISIBLE);
        gabaySynagogues_TAB.setTextColor(getResources().getColor(R.color.black));
        gabaySynagogues_TAB.setBackgroundResource(R.drawable.shape_rect04);

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
                        Toast.makeText(ShowMySynagogues.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(ShowMySynagogues.this, GabayLogin.class));
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
//                            if (synName == null) {
//                                gabaySynName.setText("");
//                            } else {
//                                gabaySynName.setText(synName);
//                            }
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



}
