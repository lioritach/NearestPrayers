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
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddSynagogue extends AppCompatActivity implements LocationListener {

    private ImageButton backBtn, gpsBtn;
    private EditText titleAddSyn, countryAddSyn, StateAddSyn, cityAddSyn,
            NegishutNehimAddSyn, NegishutAzratNashimAddSyn, FullAddressAddSyn;
    private Button addSynButton;
    private ImageView synIconAdd;
    private TextView categoryAddSyn, ShacharitAddSyn, MinhaAddSyn, ArvitAddSyn, EventsAddSyn, negishutNoteAddSyn;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_synagogue);

        //init
        backBtn = (ImageButton) findViewById(R.id.backAddSynGabayID);
        gpsBtn = (ImageButton) findViewById(R.id.gpsAddSynGabayID);
        titleAddSyn = (EditText) findViewById(R.id.titleAddSynID);
        countryAddSyn = (EditText) findViewById(R.id.CountryAddSynID);
        cityAddSyn = (EditText) findViewById(R.id.CityAddSynID);
        StateAddSyn = (EditText) findViewById(R.id.StateAddSynID);
        NegishutNehimAddSyn = (EditText) findViewById(R.id.NegishutNehimAddSynID);
        NegishutAzratNashimAddSyn = (EditText) findViewById(R.id.NegishutAzratNashimAddSynID);
        negishutNoteAddSyn = (TextView) findViewById(R.id.negishutNoteID);
        addSynButton = (Button) findViewById(R.id.addSynButtonID);
        synIconAdd = (ImageView) findViewById(R.id.synIconAddID);
        categoryAddSyn = (TextView) findViewById(R.id.categoryAddSynID);
        ShacharitAddSyn = (TextView) findViewById(R.id.ShacharitAddSynID);
        MinhaAddSyn = (TextView) findViewById(R.id.MinhaAddSynID);
        ArvitAddSyn = (TextView) findViewById(R.id.ArvitAddSynID);
        EventsAddSyn = (TextView) findViewById(R.id.EventsAddSynID);
        FullAddressAddSyn = (EditText) findViewById(R.id.fullAddressAddSynID);
        switchCompat = (SwitchCompat) findViewById(R.id.NegishutAddSynID);

        //init permissions array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("אנא המתן");
        progressDialog.setCanceledOnTouchOutside(false);

        NegishutAzratNashimAddSyn.setVisibility(View.GONE);
        NegishutNehimAddSyn.setVisibility(View.GONE);

        synIconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        categoryAddSyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick category
                categoryDialog();
            }
        });

        addSynButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1) Input data
                //2) Validate data
                //3) add data to db
                inputData();
                if(addSynagogueValidation(titleAddSyn.getText().toString(), cityAddSyn.getText().toString(), countryAddSyn.getText().toString(),
                        StateAddSyn.getText().toString(), categoryAddSyn.getText().toString(), ShacharitAddSyn.getText().toString(),
                        MinhaAddSyn.getText().toString(), ArvitAddSyn.getText().toString(), FullAddressAddSyn.getText().toString())){
                    addSynagogues();
                }
            }


        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked, show
                    NegishutAzratNashimAddSyn.setVisibility(View.VISIBLE);
                    NegishutNehimAddSyn.setVisibility(View.VISIBLE);
                } else {
                    //not checked, hide
                    NegishutAzratNashimAddSyn.setVisibility(View.GONE);
                    NegishutNehimAddSyn.setVisibility(View.GONE);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get the full address for the user
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

    private String titleNameSyn, cityAdd, countryAdd, stateAdd, categoryAdd, shacharitAdd, minhaAdd, arvitAdd,
            negishutNehim, negishutWomen, EventsAdd, fullAddressAdd, negishutNote;
    private boolean negishutAvailable = false;

    private void inputData() {

        //input data
        titleNameSyn = titleAddSyn.getText().toString().trim();
        cityAdd = cityAddSyn.getText().toString().trim();
        countryAdd = countryAddSyn.getText().toString().trim();
        stateAdd = StateAddSyn.getText().toString().trim();
        categoryAdd = categoryAddSyn.getText().toString().trim();
        shacharitAdd = ShacharitAddSyn.getText().toString().trim();
        minhaAdd = MinhaAddSyn.getText().toString().trim();
        arvitAdd = ArvitAddSyn.getText().toString().trim();
        negishutNehim = NegishutNehimAddSyn.getText().toString().trim();
        negishutWomen = NegishutAzratNashimAddSyn.getText().toString().trim();
        EventsAdd = EventsAddSyn.getText().toString().trim();
        fullAddressAdd = FullAddressAddSyn.getText().toString().trim();
        negishutAvailable = switchCompat.isChecked();

        if(negishutAvailable){
            //there is negishut
            negishutNehim = NegishutNehimAddSyn.getText().toString().trim();
            negishutWomen = NegishutAzratNashimAddSyn.getText().toString().trim();

        }

        addSynagogueValidation(titleNameSyn, cityAdd, countryAdd, stateAdd, categoryAdd, shacharitAdd, minhaAdd, arvitAdd,
                fullAddressAdd);


        //addSynagogues();

    }

    public boolean addSynagogueValidation(String titleNameSyn, String cityAdd, String countryAdd, String stateAdd, String categoryAdd, String shacharitAdd, String minhaAdd, String arvitAdd, String fullAddressAdd) {

        boolean flag = true;

        //validate data
        if (TextUtils.isEmpty(titleNameSyn)) {
            //Toast.makeText(this, "שם בית הכנסת הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            titleAddSyn.setError("שם בית הכנסת הוא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(fullAddressAdd)) {
            //Toast.makeText(this, "כתובת היא שדה חובה!", Toast.LENGTH_SHORT).show();
            FullAddressAddSyn.setError("כתובת היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(cityAdd)) {
            //Toast.makeText(this, "עיר היא שדה חובה!", Toast.LENGTH_SHORT).show();
            cityAddSyn.setError("עיר היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(countryAdd)) {
            //Toast.makeText(this, "מדינה היא שדה חובה!", Toast.LENGTH_SHORT).show();
            countryAddSyn.setError("מדינה היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(stateAdd)) {
            //Toast.makeText(this, "מחוז הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            StateAddSyn.setError("מחוז הוא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(categoryAdd)) {
            //Toast.makeText(this, "נוסח התפילה הוא שדה חובה!", Toast.LENGTH_SHORT).show();
            categoryAddSyn.setError("נוסח התפילה הוא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(shacharitAdd)) {
            //Toast.makeText(this, "שעת שחרית היא שדה חובה!", Toast.LENGTH_SHORT).show();
            ShacharitAddSyn.setError("שעת שחרית היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(minhaAdd)) {
            //Toast.makeText(this, "שעת מנחה היא שדה חובה!", Toast.LENGTH_SHORT).show();
            MinhaAddSyn.setError("שעת מנחה היא שדה חובה!");
            flag = false;
        }
        if (TextUtils.isEmpty(arvitAdd)) {
            //Toast.makeText(this, "שעת ערבית היא שדה חובה!", Toast.LENGTH_SHORT).show();
            ArvitAddSyn.setError("שעת ערבית היא שדה חובה!");
            flag = false;
        }


        return flag;

    }

    private void addSynagogues() {
        progressDialog.setMessage("מוסיף ...");
        String timestamp = "" + System.currentTimeMillis();

        if (image_uri == null) {
            //upload without image.

            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("synId", "" + timestamp);
            hashMap.put("synName", "שם בית הכנסת: " + titleNameSyn);
            hashMap.put("city", "עיר: " + cityAdd);
            hashMap.put("country", "מדינה: " + countryAdd);
            hashMap.put("fullAddress", "כתובת: " + fullAddressAdd);
            hashMap.put("category", "" + categoryAdd);
            hashMap.put("shacharit", "שעת תפילת שחרית: " + shacharitAdd);
            hashMap.put("minha", "שעת תפילת מנחה: " + minhaAdd);
            hashMap.put("arvit", "שעת תפילת ערבית: " + arvitAdd);
            hashMap.put("negishutNote", "" + negishutNote);
            hashMap.put("negishutAvailable", "נגישות: " + negishutAvailable);
            hashMap.put("negishut_nehim", "נגישות נכים: " + negishutNehim);
            hashMap.put("negishut_nashim", "עזרת נשים: " + negishutWomen);
            hashMap.put("events", "אירוע בבית הכנסת: " + EventsAdd);
            hashMap.put("synImage", "");
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("uid", "" + firebaseAuth.getUid());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).child("SynagogueDetails").child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSynagogue.this, "בית הכנסת התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSynagogue.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String filePathName = "profile_images/" + "" + firebaseAuth.getUid();
            //upload image to db
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //get url of uploaded image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()) {
                                //setup data to upload
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("synId", "" + timestamp);
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
                                hashMap.put("timestamp", "" + timestamp);
                                hashMap.put("uid", "" + firebaseAuth.getUid());

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).child("SynagogueDetails").child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddSynagogue.this, "בית הכנסת התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddSynagogue.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void clearData() {
        titleAddSyn.setText("");
        cityAddSyn.setText("");
        countryAddSyn.setText("");
        StateAddSyn.setText("");
        FullAddressAddSyn.setText("");
        categoryAddSyn.setText("");
        ShacharitAddSyn.setText("");
        MinhaAddSyn.setText("");
        ArvitAddSyn.setText("");
        NegishutNehimAddSyn.setText("");
        NegishutAzratNashimAddSyn.setText("");
        EventsAddSyn.setText("");
        synIconAdd.setImageResource(R.drawable.icons8_synagogue_40);
        image_uri = null;
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
                        categoryAddSyn.setText(category);
                    }
                })
                .show();
    }

    //when user choose to pick a image
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

    //check for location permission
    private boolean checkLocationPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;

    }

    //request for location permission
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, locationPermission, LOCATION_REQUEST_CODE);
    }

    //get the full address for the user
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

    //pick image from gallery
    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    //pick image from camera
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
                synIconAdd.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to imageview
                synIconAdd.setImageURI(image_uri);
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
            countryAddSyn.setText(country);
            cityAddSyn.setText(city);
            StateAddSyn.setText(state);
            FullAddressAddSyn.setText(address);


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
