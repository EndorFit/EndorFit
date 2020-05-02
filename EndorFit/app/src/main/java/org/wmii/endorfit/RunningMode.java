package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

public class RunningMode extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    TextView DistanceText;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode);




        final Location loc1 = new Location("");

        //loc1.setLatitude(51.1032046);
        //loc1.setLongitude(20.3595225);






        DistanceText=(TextView) findViewById(R.id.DistanceText);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);


        findViewById(R.id.StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(RunningMode.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                }
                else {
                    final Location loc1 = getLocation();
                    int delay = 5000; // number of milliseconds to sleep
                    long start = System.currentTimeMillis();
                    while(start >= System.currentTimeMillis() - delay); // do nothing
                    String z = String.valueOf(loc1.distanceTo(getLocation()));
                    DistanceText.setText(z);
                }


            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
            else {
                Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location getLocation(){
        final Location location = new Location("");

        progressBar.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(RunningMode.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(RunningMode.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int LatestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(LatestLocationIndex).getLongitude();
                            //DistanceText.setText(latitude+" "+longitude);
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, Looper.getMainLooper());
        return location;
    }




}

