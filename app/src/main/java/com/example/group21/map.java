package com.example.group21;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class map extends FragmentActivity implements OnMapReadyCallback{

    GoogleMap map;

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLang;

    DatabaseReference firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren()){
                    String lat = s.child("lat").getValue().toString();
                    String lon = s.child("lon").getValue().toString();

                    double latit = Double.parseDouble(lat);
                    double longi = Double.parseDouble(lon);
                    LatLng result = new LatLng(latit, longi);
                    map.addMarker(new MarkerOptions().position(result).title(s.child("syn").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        map = googleMap;
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                // The current location for the user
//                userLatLang = new LatLng(location.getLatitude(), location.getLongitude());
//                map.clear();
//                map.addMarker(new MarkerOptions().position(userLatLang).title("מיקומך הנוכחי"));
//
//                // list of synagogues placed in BeerSheva
//                LatLng coordinate = new LatLng(31.276773809000076, 34.81128199400007);
//                map.addMarker(new MarkerOptions().position(coordinate).title("בית כנסת רמות - הר חברון, רחוב הר תבור שכונת רמות"));
//
//                LatLng coordinate1 = new LatLng(31.27723492000007, 34.80748855500008);
//                map.addMarker(new MarkerOptions().position(coordinate1).title("בית כנסת רמות אור, רחוב הר רמון שכונת רמות"));
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };

        //askLocationPermission();

    }



    private void askLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLang = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                map.clear();
                map.addMarker(new MarkerOptions().position(userLatLang).title("Your Location"));
                map.moveCamera(CameraUpdateFactory.newLatLng(userLatLang));
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


}
