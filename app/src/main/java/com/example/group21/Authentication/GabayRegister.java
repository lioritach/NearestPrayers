package com.example.group21.Authentication;

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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.group21.MainGabayActivity;
import com.example.group21.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class GabayRegister extends AppCompatActivity{

    private ImageButton backregisterButton;
    private ImageView profileGabay;
    private EditText nameGabay, phoneGabay, passwordGabay, confirmPasswordGabay, emailGabay; //nameOfSynGabay;
    private Button registerGabay;

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

    //database
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gabay_register);

        backregisterButton = (ImageButton) findViewById(R.id.backRegisterGabayID);
        profileGabay = (ImageView) findViewById(R.id.profileGabay);
        nameGabay = (EditText) findViewById(R.id.nameGabayID);
        phoneGabay = (EditText) findViewById(R.id.phoneGabayID);
        registerGabay = (Button) findViewById(R.id.registerGabayButtonID);
        passwordGabay = (EditText) findViewById(R.id.passwordGabayID);
        confirmPasswordGabay = (EditText) findViewById(R.id.passwordAgainGabayID);
        emailGabay = (EditText) findViewById(R.id.EmailGabayID);
        //nameOfSynGabay = (EditText) findViewById(R.id.gabaySynNameID);

        //init permissions array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן");
        progressDialog.setCanceledOnTouchOutside(false);


        backregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profileGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialog();
            }
        });

        registerGabay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register user
                inputData();
                if(regValidation(nameGabay.getText().toString(), phoneGabay.getText().toString(), emailGabay.getText().toString(), passwordGabay.getText().toString(), confirmPasswordGabay.getText().toString())){
                    createAccount();
                }
            }
        });
    }

    private String FullNameGabay, PasswordGabay, ConfirmPasswordGabay, PhoneNumberGabay, EmailGabay; //SynName;
    public void inputData() {
        //input data
        FullNameGabay = nameGabay.getText().toString().trim();
        PasswordGabay = passwordGabay.getText().toString().trim();
        ConfirmPasswordGabay = confirmPasswordGabay.getText().toString().trim();
        PhoneNumberGabay = phoneGabay.getText().toString().trim();
        EmailGabay = emailGabay.getText().toString().trim();

        regValidation(FullNameGabay, PhoneNumberGabay, EmailGabay, PasswordGabay, ConfirmPasswordGabay);
    }
    public boolean regValidation(String fullname, String phone, String Email, String pass, String confPass){
        boolean flag = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

        //validate data
        if(TextUtils.isEmpty(fullname)){
            if(nameGabay!=null)
                nameGabay.setError("שם הוא שדה חובה!");
            flag = false;
        }

        if(TextUtils.isEmpty(pass)){
            //Toast.makeText(this,"סיסמא היא שדה חובה!", Toast.LENGTH_SHORT).show();
            if(passwordGabay != null)
                passwordGabay.setError("סיסמא היא שדה חובה!");
            flag = false;
        }
        if(TextUtils.isEmpty(confPass)){
            //Toast.makeText(this,"הזנת סיסמא שוב הינה שדה חובה!", Toast.LENGTH_SHORT).show();
            if(confirmPasswordGabay != null)
                confirmPasswordGabay.setError("הזנת סיסמא שוב הינה שדה חובה!");
            flag = false;
        }
        if(TextUtils.isEmpty(phone)){
            //Toast.makeText(this,"מספר טלפון הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            if(phoneGabay != null)
                phoneGabay.setError("מספר טלפון הוא שדה חובה!");
            flag = false;
        }
        if(!Email.isEmpty()){
            if(!Email.trim().matches(emailPattern)) {
                //Toast.makeText(this,"המייל אינו תקין", Toast.LENGTH_SHORT).show();
                if (emailGabay != null)
                    emailGabay.setError("המייל אינו תקין!");
                flag = false;
            }
        }
        if(pass.length() < 6){
            //Toast.makeText(this,"הסיסמא יכולה להכיל לפחות 6 תווים", Toast.LENGTH_SHORT).show();
            if(passwordGabay != null)
                passwordGabay.setError("הסיסמא יכולה להכיל לפחות 6 תווים");
            flag = false;
        }
        if(!pass.equals(confPass)){
            //Toast.makeText(this,"הסיסמא שהזנת לא תואמת לסיסמא המקורית", Toast.LENGTH_SHORT).show();
            if(confirmPasswordGabay != null)
                confirmPasswordGabay.setError("הסיסמא שהזנת לא תואמת לסיסמא המקורית");
            flag = false;
        }

        return flag;
    }


    private void createAccount(){
        progressDialog.setMessage("יוצר חשבון...");
        progressDialog.show();

        //create account
        firebaseAuth.createUserWithEmailAndPassword(EmailGabay, PasswordGabay)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created
                        saveFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed creating account
                        progressDialog.dismiss();
                        Toast.makeText(GabayRegister.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveFirebaseData() {
        progressDialog.setMessage("שומר את פרטי החשבון ...");
        String timestamp = ""+System.currentTimeMillis();
        if(image_uri == null){
            //save without image
            //setup data to save
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("name",""+FullNameGabay);
            hashMap.put("email",""+EmailGabay);
            hashMap.put("phone",""+PhoneNumberGabay);
            //hashMap.put("synagogueName",""+SynName);
            hashMap.put("profileImage", "");
            hashMap.put("accountType", "גבאי");
            hashMap.put("online", "true");
            hashMap.put("timestamp", ""+timestamp);

            //save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //db update
                            progressDialog.dismiss();
                            startActivity(new Intent(GabayRegister.this, MainGabayActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed update db
                            progressDialog.dismiss();
                            startActivity(new Intent(GabayRegister.this, MainGabayActivity.class));
                            finish();
                        }
                    });
        }
        else{
            //save info with image
            //name and path of image
            String filePathName = "profile_images/" + ""+firebaseAuth.getUid();
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //get url of uploaded image
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
                                //hashMap.put("synagogueName",""+SynName);
                                hashMap.put("profileImage", ""+downloadImageUri);
                                hashMap.put("accountType", "גבאי");
                                hashMap.put("online", "true");
                                hashMap.put("timestamp", ""+timestamp);

                                //save to db
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //db update
                                                progressDialog.dismiss();
                                                startActivity(new Intent(GabayRegister.this, MainGabayActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed update db
                                                progressDialog.dismiss();
                                                startActivity(new Intent(GabayRegister.this, MainGabayActivity.class));
                                                finish();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(GabayRegister.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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

    private void showImagePickDialog(){
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

    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //get picked img
                image_uri = data.getData();
                //set to imageview
                profileGabay.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to imageview
                profileGabay.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
