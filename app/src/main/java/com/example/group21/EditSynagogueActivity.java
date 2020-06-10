package com.example.group21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;
import java.util.Locale;

public class EditSynagogueActivity extends AppCompatActivity implements LocationListener {

    private ImageButton backBtn, gpsBtn;
    private EditText titleUpdateyn, countryUpdateSyn, StateUpdateSyn, cityUpdateSyn,
            NegishutNehimUpdateSyn, NegishutAzratNashimUpdateSyn, FullAddressUpdateSyn;
    private Button updateSynButton;
    private ImageView synIconUpdate;
    private TextView categoryUpdateSyn, ShacharitUpdateSyn, MinhaUpdateSyn, ArvitUpdateSyn, EventsAddSyn, negishutNoteUpdateSyn;
    private SwitchCompat switchCompat;

    //permission const
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick const
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] locationPermission;
    private String[] cameraPermission;
    private String[] storagePermission;

    private LocationManager locationManager;
    private double latitude, longitude;

    //image picked uri
    private Uri image_uri;

    //database
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private String synId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_synagogue);

        //init
        backBtn = (ImageButton) findViewById(R.id.backAddSynGabayID);
        gpsBtn = (ImageButton) findViewById(R.id.gpsAddSynGabayID);
        titleUpdateyn = (EditText) findViewById(R.id.titleAddSynID);
        countryUpdateSyn = (EditText) findViewById(R.id.CountryAddSynID);
        cityUpdateSyn = (EditText) findViewById(R.id.CityAddSynID);
        StateUpdateSyn = (EditText) findViewById(R.id.StateAddSynID);
        NegishutNehimUpdateSyn = (EditText) findViewById(R.id.NegishutNehimAddSynID);
        NegishutAzratNashimUpdateSyn = (EditText) findViewById(R.id.NegishutAzratNashimAddSynID);
        negishutNoteUpdateSyn = (TextView) findViewById(R.id.negishutNoteID);
        updateSynButton = (Button) findViewById(R.id.updateSynButtonID);
        synIconUpdate = (ImageView) findViewById(R.id.synIconAddID);
        categoryUpdateSyn = (TextView) findViewById(R.id.categoryAddSynID);
        ShacharitUpdateSyn = (TextView) findViewById(R.id.ShacharitAddSynID);
        MinhaUpdateSyn = (TextView) findViewById(R.id.MinhaAddSynID);
        ArvitUpdateSyn = (TextView) findViewById(R.id.ArvitAddSynID);
        EventsAddSyn = (TextView) findViewById(R.id.EventsAddSynID);
        FullAddressUpdateSyn = (EditText) findViewById(R.id.fullAddressAddSynID);
        switchCompat = (SwitchCompat) findViewById(R.id.NegishutAddSynID);

        //init permissions array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};


        //get id of the synagogue from intent
        synId = getIntent().getStringExtra("synId");

        NegishutAzratNashimUpdateSyn.setVisibility(View.GONE);
        NegishutNehimUpdateSyn.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        loadSynDetails(); //to set on views

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן");
        progressDialog.setCanceledOnTouchOutside(false);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked, show
                    NegishutAzratNashimUpdateSyn.setVisibility(View.VISIBLE);
                    NegishutNehimUpdateSyn.setVisibility(View.VISIBLE);
                } else {
                    //not checked, hide
                    NegishutAzratNashimUpdateSyn.setVisibility(View.GONE);
                    NegishutNehimUpdateSyn.setVisibility(View.GONE);
                }
            }
        });

        synIconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        categoryUpdateSyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick category
                categoryDialog();
            }
        });

        updateSynButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) Input data
                //2) Validate data
                //3) update data to db
                inputData();
                if(editValidation(titleUpdateyn.getText().toString(), cityUpdateSyn.getText().toString(), countryUpdateSyn.getText().toString(), StateUpdateSyn.getText().toString(), categoryUpdateSyn.getText().toString(), ShacharitUpdateSyn.getText().toString(),
                        MinhaUpdateSyn.getText().toString(), ArvitUpdateSyn.getText().toString(), FullAddressUpdateSyn.getText().toString())){
                    updateSyn();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //detect current location
                if (checkLocationPermission()) {
                    //already allowed
                    detectLocation();
                } else {
                    //not allowed
                    requestLocationPermission();
                }

            }
        });

    }

    private void loadSynDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("SynagogueDetails").child(synId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get data
                        String id = ""+dataSnapshot.child("synId").getValue();
                        String synName = ""+dataSnapshot.child("synName").getValue();
                        String fullAddress = ""+dataSnapshot.child("fullAddress").getValue();
                        String category = ""+dataSnapshot.child("category").getValue();
                        String shacharit = ""+dataSnapshot.child("shacharit").getValue();
                        String minha = ""+dataSnapshot.child("minha").getValue();
                        String arvit = ""+dataSnapshot.child("arvit").getValue();
                        String negishut_nehim = ""+dataSnapshot.child("negishut_nehim").getValue();
                        String negishut_nashim = ""+dataSnapshot.child("negishut_nashim").getValue();
                        String events = ""+dataSnapshot.child("events").getValue();
                        String synImage = ""+dataSnapshot.child("synImage").getValue();
                        String timestamp = ""+dataSnapshot.child("timestamp").getValue();
                        String uid = ""+dataSnapshot.child("uid").getValue();
                        String negishotNote = ""+dataSnapshot.child("negishotNote").getValue();
                        String negishutAvailable = ""+dataSnapshot.child("negishutAvailable").getValue();

                        //set data to views
                        if(negishutAvailable.equals("true")){
                            switchCompat.setChecked(true);
                            NegishutAzratNashimUpdateSyn.setVisibility(View.VISIBLE);
                            NegishutNehimUpdateSyn.setVisibility(View.VISIBLE);
                        }
                        else{
                            switchCompat.setChecked(false);
                            NegishutAzratNashimUpdateSyn.setVisibility(View.GONE);
                            NegishutNehimUpdateSyn.setVisibility(View.GONE);
                        }

                        titleUpdateyn.setText(synName);
                        FullAddressUpdateSyn.setText(fullAddress);
                        categoryUpdateSyn.setText(category);
                        ShacharitUpdateSyn.setText(shacharit);
                        MinhaUpdateSyn.setText(minha);
                        ArvitUpdateSyn.setText(arvit);
                        NegishutNehimUpdateSyn.setText(negishut_nehim);
                        NegishutAzratNashimUpdateSyn.setText(negishut_nashim);
                        EventsAddSyn.setText(events);
                        //negishutNoteUpdateSyn.setText(negishotNote);

                        try{
                            Picasso.get().load(synImage).placeholder(R.drawable.icons8_synagogue_40).into(synIconUpdate);
                        }catch (Exception e){
                            synIconUpdate.setImageResource(R.drawable.icons8_synagogue_40);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private String titleNameSyn, cityAdd, countryAdd, stateAdd, categoryAdd, shacharitAdd, minhaAdd, arvitAdd,
            negishutNehim, negishutWomen, EventsAdd, fullAddressAdd, negishutNote, lat, lon;
    private boolean negishutAvailable = false;

    private void inputData() {
        //input data
        titleNameSyn = titleUpdateyn.getText().toString().trim();
        cityAdd = cityUpdateSyn.getText().toString().trim();
        countryAdd = countryUpdateSyn.getText().toString().trim();
        stateAdd = StateUpdateSyn.getText().toString().trim();
        categoryAdd = categoryUpdateSyn.getText().toString().trim();
        shacharitAdd = ShacharitUpdateSyn.getText().toString().trim();
        minhaAdd = MinhaUpdateSyn.getText().toString().trim();
        arvitAdd = ArvitUpdateSyn.getText().toString().trim();
        negishutNehim = NegishutNehimUpdateSyn.getText().toString().trim();
        negishutWomen = NegishutAzratNashimUpdateSyn.getText().toString().trim();
        EventsAdd = EventsAddSyn.getText().toString().trim();
        fullAddressAdd = FullAddressUpdateSyn.getText().toString().trim();
        negishutAvailable = switchCompat.isChecked();

        editValidation(titleNameSyn, cityAdd, countryAdd, stateAdd, categoryAdd, shacharitAdd, minhaAdd, arvitAdd, fullAddressAdd);

    }

    public boolean editValidation(String titleNameSyn, String cityAdd, String countryAdd, String stateAdd, String categoryAdd, String shacharitAdd, String minhaAdd, String arvitAdd, String fullAddressAdd) {

        boolean flag = true;

        //validate data
        if (TextUtils.isEmpty(titleNameSyn)) {
            //Toast.makeText(this, "שם בית הכנסת הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            titleUpdateyn.setError("שם בית הכנסת הוא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(fullAddressAdd)) {
            //Toast.makeText(this, "כתובת היא שדה חובה!", Toast.LENGTH_SHORT).show();
            FullAddressUpdateSyn.setError("כתובת היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(cityAdd)) {
            //Toast.makeText(this, "עיר היא שדה חובה!", Toast.LENGTH_SHORT).show();
            cityUpdateSyn.setError("עיר היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(countryAdd)) {
            //Toast.makeText(this, "מדינה היא שדה חובה!", Toast.LENGTH_SHORT).show();
            countryUpdateSyn.setError("מדינה היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(stateAdd)) {
            //Toast.makeText(this, "מחוז הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            StateUpdateSyn.setError("מחוז הוא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(categoryAdd)) {
            //Toast.makeText(this, "קטגוריה היא שדה חובה!", Toast.LENGTH_SHORT).show();
            categoryUpdateSyn.setError("קטגוריה היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(shacharitAdd)) {
            //Toast.makeText(this, "שעת שחרית היא שדה חובה!", Toast.LENGTH_SHORT).show();
            ShacharitUpdateSyn.setError("שעת שחרית היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(minhaAdd)) {
            //Toast.makeText(this, "שעת מנחה היא שדה חובה!", Toast.LENGTH_SHORT).show();
            MinhaUpdateSyn.setError("שעת מנחה היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(arvitAdd)) {
            //Toast.makeText(this, "שעת ערבית היא שדה חובה!", Toast.LENGTH_SHORT).show();
            ArvitUpdateSyn.setError("שעת ערבית היא שדה חובה!");
            flag = false;
        }

        if(negishutAvailable){
            //there is negishut
            negishutNehim = NegishutNehimUpdateSyn.getText().toString().trim();
            negishutWomen = NegishutAzratNashimUpdateSyn.getText().toString().trim();

        } else{
            negishutNehim = "אין";
            negishutWomen = "אין";
        }

        return flag;
    }

    private void updateSyn() {
        //show progress
        progressDialog.setMessage("מעדכן פרטי בית הכנסת ...");
        progressDialog.show();

        if(image_uri == null){
            //update without image

            //setup data to hashmap to update
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("synName", "" + titleNameSyn);
            hashMap.put("city", "" + cityAdd);
            hashMap.put("country", "" + countryAdd);
            hashMap.put("fullAddress", "" + fullAddressAdd);
            hashMap.put("category", "" + categoryAdd);
            hashMap.put("shacharit", "" + shacharitAdd);
            hashMap.put("minha", "" + minhaAdd);
            hashMap.put("arvit", "" + arvitAdd);
            hashMap.put("negishutNote", "" + negishutNote);
            hashMap.put("negishutAvailable", "" + negishutAvailable);
            hashMap.put("negishut_nehim", "" + negishutNehim);
            hashMap.put("negishut_nashim", "" + negishutWomen);
            hashMap.put("events", "" + EventsAdd);

            //update to db
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("SynagogueDetails").child(synId)
                    .updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //update success
                            progressDialog.dismiss();
                            Toast.makeText(EditSynagogueActivity.this, "הפרטים עודכנו", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //updated failed
                            Toast.makeText(EditSynagogueActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else{
            //update with image

            //first upload image
            //image name and path to db storage
            String filePathName = "profile_images/" + synId;
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image uploaded, get url of image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()){
                                //setup data to hashmap
                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("synName", "" + titleNameSyn);
                                hashMap.put("city", "" + cityAdd);
                                hashMap.put("country", "" + countryAdd);
                                hashMap.put("fullAddress", "" + fullAddressAdd);
                                hashMap.put("category", "" + categoryAdd);
                                hashMap.put("shacharit", "" + shacharitAdd);
                                hashMap.put("minha", "" + minhaAdd);
                                hashMap.put("arvit", "" + arvitAdd);
                                hashMap.put("negishutNote", "" + negishutNote);
                                hashMap.put("negishutAvailable", "" + negishutAvailable);
                                hashMap.put("negishut_nehim", "" + negishutNehim);
                                hashMap.put("negishut_nashim", "" + negishutWomen);
                                hashMap.put("events", "" + EventsAdd);
                                hashMap.put("synImage", "" + downloadImageUri);

                                //update to db
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).child("SynagogueDetails").child(synId)
                                        .updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //update success
                                                progressDialog.dismiss();
                                                Toast.makeText(EditSynagogueActivity.this, "מעדכן פרטי בית הכנסת", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //updated failed
                                                Toast.makeText(EditSynagogueActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר את נוסח התפילה")
                .setItems(Constants.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String category = Constants.options[which];
                        //set picked category
                        categoryUpdateSyn.setText(category);
                    }
                })
                .show();
    }

    private void showImagePickDialog() {
        String[] options = {"מצלמה", "גלרייה"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר תמונה")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //camera clicked
                            if (checkCameraPermission()) {
                                //camera permission allowed
                                pickFromCamera();
                            } else {
                                //not allowed, request
                                requestCameraPermission();
                            }
                        } else {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                //storage permission allowed
                                pickFromGallery();
                            } else {
                                //not allowed, request
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private boolean checkLocationPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;

    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, locationPermission, LOCATION_REQUEST_CODE);
    }

    private void detectLocation() {
        Toast.makeText(this, "אנא המתן ...", Toast.LENGTH_LONG).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_image_Description");

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
            case LOCATION_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted){
                        //permission allowed
                        detectLocation();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "צריך הרשאה למיקום", Toast.LENGTH_SHORT).show();
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
                synIconUpdate.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to imageview
                synIconUpdate.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLocationChanged(Location location) {
        //location detected
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        findAddress();
    }

    private void findAddress() {
        //find address, country, state and city
        Geocoder geocoder;
        List<Address> addresses;
        double latAdd, lonAdd;
        geocoder = new Geocoder(this, Locale.getDefault());

        try{

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); //complete address
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            //set addresses
            countryUpdateSyn.setText(country);
            cityUpdateSyn.setText(city);
            StateUpdateSyn.setText(state);
            FullAddressUpdateSyn.setText(address);



        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //gps or location disabled
        Toast.makeText(this,"אנא הפעל את המיקום", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
