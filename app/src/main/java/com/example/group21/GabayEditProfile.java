package com.example.group21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.group21.Authentication.GabayLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class GabayEditProfile extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView editProfileGabay;
    private EditText gabayEditName, gabayEditMail, gabayEditPhone, gabayEditSynName;
    private Button updateGabayProfile;

    //permission const
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick const
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermission;
    private String[] storagePermission;

    //image picked uri
    private Uri image_uri;

    //progress dialog
    private ProgressDialog progressDialog;

    //db
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gabay_edit_profile);

        backBtn = (ImageButton) findViewById(R.id.backEditProfileGabayID);
        editProfileGabay = (ImageView) findViewById(R.id.profileGabay);
        gabayEditName = (EditText) findViewById(R.id.nameGabayID);
        gabayEditMail = (EditText) findViewById(R.id.EmailGabayID);
        gabayEditPhone = (EditText) findViewById(R.id.phoneGabayID);
        gabayEditSynName = (EditText) findViewById(R.id.editNameSynID);
        updateGabayProfile = (Button) findViewById(R.id.updateGabayProfileID);

        //init permissions array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back activity
                onBackPressed();
            }
        });

        updateGabayProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update details
                updateData();
                if(editprofileValidation(gabayEditName.getText().toString(), gabayEditPhone.getText().toString(), gabayEditMail.getText().toString())){
                    updateProfile();
                }
            }
        });

        editProfileGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialog();
            }
        });
    }

    private String FullNameGabay, PhoneNumberGabay, EmailGabay, ChangeSynName;
    private void updateData() {
        //input data
        FullNameGabay = gabayEditName.getText().toString().trim();
        PhoneNumberGabay = gabayEditPhone.getText().toString().trim();
        EmailGabay = gabayEditMail.getText().toString().trim();
        ChangeSynName = gabayEditSynName.getText().toString().trim();

        //updateProfile();
        editprofileValidation(FullNameGabay, PhoneNumberGabay, EmailGabay);
    }

    public boolean editprofileValidation(String fullName, String phoneNumber, String email){

        boolean flag = true;

        //validate data
        if(TextUtils.isEmpty(fullName)){
            gabayEditName.setError("אין אפשרות להשאיר שדה זה ריק");
            flag = false;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            gabayEditPhone.setError("אין אפשרות להשאיר שדה זה ריק");
            flag = false;
        }
        if(TextUtils.isEmpty(email)){
            gabayEditMail.setError("אין אפשרות להשאיר שדה זה ריק");
            flag = false;
        }

        return flag;
    }

    private void updateProfile() {
        progressDialog.setMessage("מעדכן פרופיל ...");
        progressDialog.show();

        if(image_uri == null){
            //update without image
            //setup data to save
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("name",""+FullNameGabay);
            hashMap.put("email",""+EmailGabay);
            hashMap.put("phone",""+PhoneNumberGabay);
            hashMap.put("synagogueName",""+ChangeSynName);
            hashMap.put("profileImage", "");
            hashMap.put("accountType", "גבאי");
            hashMap.put("online", "true");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //db update
                            progressDialog.dismiss();
                            Toast.makeText(GabayEditProfile.this, "הפרופיל עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed update db
                            progressDialog.dismiss();
                            Toast.makeText(GabayEditProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            String filePathName = "profile_images/" + ""+firebaseAuth.getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()){
                                //setup data to save
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid",""+firebaseAuth.getUid());
                                hashMap.put("name",""+FullNameGabay);
                                hashMap.put("email",""+EmailGabay);
                                hashMap.put("phone",""+PhoneNumberGabay);
                                hashMap.put("synagogueName",""+ChangeSynName);
                                hashMap.put("profileImage", ""+downloadImageUri);
                                hashMap.put("accountType", "גבאי");
                                hashMap.put("online", "true");

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //db update
                                                progressDialog.dismiss();
                                                Toast.makeText(GabayEditProfile.this, "הפרופיל עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed update db
                                                progressDialog.dismiss();
                                                Toast.makeText(GabayEditProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(GabayEditProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(GabayEditProfile.this, GabayLogin.class));
            finish();
        }
        else{
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String synName = ""+ds.child("synagogueName").getValue();
                            String online = ""+ds.child("online").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();
                            String uid = ""+ds.child("uid").getValue();

                            gabayEditName.setText(name);
                            gabayEditMail.setText(email);
                            gabayEditPhone.setText(phone);
                            gabayEditSynName.setText(synName);

                            try{
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_gray).into(editProfileGabay);
                            }catch (Exception e){
                                editProfileGabay.setImageResource(R.drawable.ic_person_gray);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showImagePickDialog() {
        String[] options = {"מצלמה", "גלרייה"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר תמונה")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            //camera clicked
                            if(checkCameraPermission()){
                                //camera permission allowed
                                pickFromCamera();
                            }
                            else{
                                //not allowed, request
                                requestCameraPermission();
                            }
                        }
                        else{
                            //gallery clicked
                            if(checkStoragePermission()){
                                //storage permission allowed
                                pickFromGallery();
                            }
                            else{
                                //not allowed, request
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //permission allowed
                        pickFromCamera();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "צריך הרשאה למצלמה", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //permission allowed
                        pickFromGallery();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "צריך הרשאה לאחסון", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //get picked img
                image_uri = data.getData();
                //set to imageview
                editProfileGabay.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to imageview
                editProfileGabay.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
