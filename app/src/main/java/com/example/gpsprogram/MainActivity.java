package com.example.gpsprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Magnifier;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText Latitude, Longitude;
    Button GetLocation;
    TextView Address;

    public LocationManager LM;
    public LocationListener LS = new MyLS();

    String lat, log;
    private Boolean isGPSAvailable = false;
    private Boolean isNetworkAvailable = false;

    //To Generate Address
    Geocoder geocoder;
    List<Address> myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Latitude = (EditText) findViewById(R.id.Latitude);
        Longitude = (EditText) findViewById(R.id.Longitude);
        GetLocation = (Button) findViewById(R.id.GetLocation);
        Address = (TextView) findViewById(R.id.Address);

        LM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMyLocation();
            }
        });

        checkLocationPermission();

    }

    class MyLS implements LocationListener {


        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                LM.removeUpdates(LS);
                lat = "" + location.getLatitude();
                log = "" + location.getLongitude();
                Latitude.setText(lat);
                Longitude.setText(log);
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    myAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address1 = myAddress.get(0).getAddressLine(0);
                Address.setText(address1);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    public void GetMyLocation() {
        try {
            isGPSAvailable = LM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception exp) {

        }
        try {
            isNetworkAvailable = LM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception exp) {

        }
        if (!isGPSAvailable && !isNetworkAvailable) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Attention");
            builder.setMessage("Sorry, location is not available, please Enable location services");
            builder.create().show();
        }
        if (isGPSAvailable) {

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
            LM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, LS);
        }
        if (isNetworkAvailable) {
            LM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,LS);
        }
    }

    private boolean checkLocationPermission() {
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //complete string of address in list
        List<String>listPermission = new ArrayList<>();
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]),1);
        }
        return true;
    }

}