package com.example.visitid.mainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.visitid.R;
import com.example.visitid.userAuth.signUp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class mapList extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = mapList.class.getSimpleName();
    private GoogleMap mMap;



    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(39.954708, -75.186974);
    private static final int DEFAULT_ZOOM = 15;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();



    List<listtem> liveList;
    ListView listView;

  //  DatabaseReference location, eventData, eventAn, tempDataRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_list);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




//        tempDataRef = FirebaseDatabase.getInstance().getReference().child("Events").child("hs7vsjv8es_");
//        location = tempDataRef.child("location");
//        eventData = tempDataRef.child("eventData");
//        eventAn = eventData.child("analytics");
//        location.child("eventLoc").child("lat").setValue("39.954708");
//        location.child("eventLoc").child("long").setValue("-75.186974");
//        location.child("eventLoc").child("radius").setValue("100");
//        eventData.child("eventStartTime").setValue("2020-02-22");
//        eventData.child("eventEndTime").setValue("2020-02-23");
//        eventData.child("organizerName").setValue("MLH");
//        eventData.child("eventName").setValue("Dragonhacks 2020");
//        eventData.child("eventLocation").setValue("Bossone Research Facility");
//        eventAn.child("numPeople").setValue(15);
//        eventAn.child("listOfPeople").child("userID1").child("email").setValue("test1@gmail.com");
//        eventAn.child("listOfPeople").child("userID1").child("name").setValue("Name1");
//        eventAn.child("listOfPeople").child("userID1").child("time").setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() + (604800000L * 2) + (24 * 60 * 60))));
//        eventAn.child("listOfPeople").child("userID2").child("email").setValue("test2@gmail.com");
//        eventAn.child("listOfPeople").child("userID2").child("name").setValue("Name2");
//        eventAn.child("listOfPeople").child("userID1").child("time").setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() + (604800000L * 2) + (24 * 60 * 60))));
//        eventAn.child("listOfPeople").child("userID3").child("email").setValue("test3@gmail.com");
//        eventAn.child("listOfPeople").child("userID3").child("name").setValue("Name3");
//        eventAn.child("listOfPeople").child("userID1").child("time").setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() + (604800000L * 2) + (24 * 60 * 60))));

        liveList = new ArrayList<>();


        myRef.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){

                    String loc = item.child("eventData").child("eventAddress").getValue().toString();
                    String nameE = item.child("eventData").child("eventName").getValue().toString();
                    String nameO = item.child("eventData").child("organizerName").getValue().toString();
                    String endT = item.child("eventData").child("eventEndTime").getValue().toString();
                    String strt = item.child("eventData").child("eventStartTime").getValue().toString();
                    String bim = item.child("eventData").child("eventLogo").getValue().toString();
                    String temp = item.getKey();



                    liveList.add(new listtem(nameE, nameO, strt, endT, loc, bim, temp));
                    //Toast.makeText(mapList.this, liveList.get(0).getLocation(), Toast.LENGTH_LONG).show();
                }
                customAdapter adapter = new customAdapter(mapList.this, R.layout.listtemp, liveList);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mapList.this, joinEvent.class);
                        intent.putExtra("Event_Name", liveList.get(position).getKey());
                        startActivity(intent);
                    }
                });
                for(int i = 0; i < liveList.size(); i ++){
                    getLocationFromAddress(liveList.get(i).getLocation());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Toast.makeText(mapList.this, liveList.get(0).getLocation(), Toast.LENGTH_LONG).show();

//        String loc = "asdfasd";
//        String nameE = "badsf";
//        String nameO = "asdf";
//        String endT = "1231";
//        liveList.add(new listtem(nameE, nameO, endT, loc));
//        Toast.makeText(mapList.this, liveList.get(1).getLocation(), Toast.LENGTH_LONG).show();
//        Toast.makeText(mapList.this, liveList.get(2).getLocation(), Toast.LENGTH_LONG).show();
//        Toast.makeText(mapList.this, liveList.get(3).getLocation(), Toast.LENGTH_LONG).show();




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }


        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }

    }
    public void getLocationFromAddress(String strAddress)
    {
        //Create coder with Activity context - this
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress,5);

            //check for null
            if (address == null) {
                return;
            }

            //Lets take first possibility from the all possibilities.
            Address location=address.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            //Put marker on map on that LatLng
            mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));



            //Animate and Zoon on that map location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}


