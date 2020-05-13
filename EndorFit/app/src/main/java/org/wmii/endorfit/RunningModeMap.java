package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Vector;

public class RunningModeMap extends FragmentActivity implements OnMapReadyCallback {

    protected Vector<Location> route = new Vector<Location>();

    boolean clicked=false;
    Button startButton;
    Button stopButton;

    TextView textView;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_map);




        startButton =(Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setEnabled(false);
        stopButton.setVisibility(View.GONE);



        textView = (TextView) findViewById(R.id.textView3) ;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);





            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setTextSize(25);
                    textView.setText("RUN!");
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    startButton.setVisibility(View.GONE);
                    stopButton.setVisibility(View.VISIBLE);
                    clicked=true;


                    if (ActivityCompat.checkSelfPermission(RunningModeMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunningModeMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RunningModeMap.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    } else {
                    fetchLastLocation();}


                }
            });
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    stopButton.setVisibility(View.GONE);
                    startButton.setVisibility(View.VISIBLE);
                    clicked=false;
                    textView.setTextSize(15);
                    //textView.setText("DISTANCE: \n"+getTotalDistance()/1000+"km\n"+getTotalDistance()+"m");
                    textView.setText(String.format("DISTANCE: \n%.2f km\n%.2f m",getTotalDistance()/1000,getTotalDistance()));

                }
            });

    }



    private void fetchLastLocation() {
        final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(RunningModeMap.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(RunningModeMap.this)
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                if(clicked==true) {
                                    int LatestLocationIndex = locationResult.getLocations().size() - 1;

                                double latitude =
                                        locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                                double longitude =
                                        locationResult.getLocations().get(LatestLocationIndex).getLongitude();
                                route.add(locationResult.getLocations().get(LatestLocationIndex));
                                currentLocation=locationResult.getLocations().get(LatestLocationIndex);
                               // textView.setText("LAT :"+ latitude+"\nLON : "+longitude);
                                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                                supportMapFragment.getMapAsync(RunningModeMap.this);



                                            pause(2000);
                                            fetchLastLocation();
                                        }

                            }
                        }
                    }, Looper.getMainLooper());
        }

    public double getTotalDistance() {
        double distance = 0;
        if(route.size() > 1) {
            for (Integer i = 0; i < route.size() - 1; i++) {
                distance += (route.get(i).distanceTo(route.get(i + 1)));
            }
        }
        return distance;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                MarkerOptions startMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                if(route.size()==1){
                startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_marker));
                googleMap.addMarker(startMarker);
                startMarker.title("START");}
                if(route.size()>1){
                    startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_marker));
                    googleMap.addMarker(startMarker);
                    startMarker.title(""+route.size());
                }



           // textView.setText("AKTUALNE POLOZENIE: "+currentLocation.getLatitude()+", "+currentLocation.getLongitude());


        LatLng End = latLng;
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(End));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(End, 14));
        //googleMap.getUiSettings().setScrollGesturesEnabled(false);
       // googleMap.getUiSettings().setZoomGesturesEnabled(false);





    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    public static void pause(long timeInMilliSeconds) {

        long timestamp = System.currentTimeMillis();


        do {

        } while (System.currentTimeMillis() < timestamp + timeInMilliSeconds);

    }

}
