package com.emre.javamapsexamp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.emre.javamapsexamp.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MapsActivity<Public> extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastLocation;
    SharedPreferences sharedPreferences;
    Boolean info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerLauncher();
        sharedPreferences = this.getSharedPreferences("com.emre.javamapsexamp", MODE_PRIVATE);
        info = false;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        //casting
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                info = sharedPreferences.getBoolean("ed", false);
                if (!info) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    sharedPreferences.edit().putBoolean("ed", true).apply();
                }


            }

        };

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Permission needed for maps", Snackbar.LENGTH_INDEFINITE).
                        setAction("Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //request permission
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                            }
                        }).show();
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
            }
            mMap.setMyLocationEnabled(true);
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        //latitude = enlem
        //longitude= boylam
        //41.0255702,28.974379

        // LatLng galata = new LatLng(41.0255702, 28.974379);
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(galata, 18));
        // mMap.addMarker(new MarkerOptions().position(galata).title("Galata Tower"));


    }

    private void registerLauncher() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            if (ContextCompat.checkSelfPermission(
                                    MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                                if (lastLocation != null) {
                                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
                                }

                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "Permission Needed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onMapLongClick(LatLng kullanicisecimi) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(kullanicisecimi));

    }
}