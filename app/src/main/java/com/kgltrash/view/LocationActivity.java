package com.kgltrash.view;

import static com.google.android.gms.maps.MapsInitializer.initialize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kgltrash.R;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.gms.location.LocationRequest;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Author: Aanuoluwapo Orioke
 */
public class LocationActivity extends AppCompatActivity {

    private LocationRequest locationRequest;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private TextView city;
    private Button doneButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_location_activity);

        Button searchLocationButton = (Button) findViewById(R.id.search_location_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_map_button);
        doneButton = (Button) findViewById(R.id.register_button);
        city = (TextView) findViewById(R.id.city);
        initialize(this);


        searchLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        startMapFragment();

                    } else {
                        checkLocationPermission();
                    }
                } else {
                    startMapFragment();
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToRegistrationIntentData = new Intent();
                backToRegistrationIntentData.putExtra("city", city.getText().toString());
                setResult(RESULT_OK, backToRegistrationIntentData);
                finish();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 199;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                        }, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                       startMapFragment();
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void startMapFragment() {
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            checkLocationPermission();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        location = locationManager.getLastKnownLocation("gps");
        Bundle bundle = new Bundle();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        if (!city.getText().toString().equalsIgnoreCase("")) {
            bundle.putString("city", city.getText().toString());
        }
        MapsFragment mapFragment = new MapsFragment();
        mapFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, mapFragment)
                .commit();
        doneButton.setVisibility(View.VISIBLE);

        String cityName = null;
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() != 0) {
                cityName = addresses.get(0).getLocality();
                city.setText(cityName);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location_) {
            city.setText("");
            location = location_;
            longitude = location_.getLongitude();
            latitude = location_.getLatitude();

            //get city name
            String cityName = null;
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (addresses.size() != 0) {
                    cityName = addresses.get(0).getLocality();
                    city.setText(cityName);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}