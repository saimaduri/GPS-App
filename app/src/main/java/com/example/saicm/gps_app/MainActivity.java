package com.example.saicm.gps_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvlatitude;
    TextView tvlongitude;
    TextView tvaddress;
    TextView tvdistance;

    ArrayList<Address> addresslist;

    double latitude;
    double longitude;
    double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvlatitude = findViewById(R.id.latitude);
        tvlongitude = findViewById(R.id.longitude);
        tvaddress = findViewById(R.id.address);
        tvdistance = findViewById(R.id.distance);

        final Geocoder geocoder = new Geocoder(this, Locale.US);
        Log.d("TAG", "onCreate: " + geocoder.isPresent());
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        addresslist = new ArrayList<>();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tvlatitude.setText(latitude + "");
                tvlongitude.setText(longitude + "");

                try {
                    addresslist.add(geocoder.getFromLocation(latitude, longitude, 1).get(0));
                    Log.d("CHECK GEOCODER", "onLocationChanged: " + addresslist);
                    tvaddress.setText(addresslist.get(addresslist.size()-1).getAddressLine(0)+"");
                    if (addresslist.size() > 1){
                        Location last = new Location("last location");
                        last.setLatitude(addresslist.get(addresslist.size()-2).getLatitude());
                        last.setLongitude(addresslist.get(addresslist.size()-2).getLongitude());
                        distance+=location.distanceTo(last);
                        tvdistance.setText("Distance so far: " + distance/1609.344 + " miles");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);


    }
}

