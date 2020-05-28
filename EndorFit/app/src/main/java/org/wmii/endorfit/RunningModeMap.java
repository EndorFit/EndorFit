package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Vector;

public class RunningModeMap extends FragmentActivity implements OnMapReadyCallback {

    protected Vector<Location> route = new Vector<Location>();


    long timer=0;

    boolean clicked=false;
    Button startButton;
    Button stopButton;
    Button newRunButton;

    TextView textSpeed;
    TextView textDistance;
    TextView textTime;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    LatLng oldLatLng;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_map);




        startButton =(Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        newRunButton = (Button) findViewById(R.id.newRunButton);

        stopButton.setEnabled(false);
        stopButton.setVisibility(View.GONE);


        textSpeed = (TextView) findViewById(R.id.textSpeed) ;
        textDistance = (TextView) findViewById(R.id.textDistance) ;
        textTime = (TextView) findViewById(R.id.textTime) ;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "RUN!", Toast.LENGTH_SHORT).show();
                    timer= System.currentTimeMillis();
                    textDistance.setTextSize(25);
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

                    final AlertDialog.Builder builder = new AlertDialog.Builder(RunningModeMap.this);
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.warning_icon_default);
                    builder.setMessage("Are you sure to stop?");
                    builder.setTitle("Please, Confirm...");
                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            long timerEnd = (System.currentTimeMillis()-timer)/1000;
                            double totalDistance=getTotalDistance();
                            double speed=getSpeed(totalDistance,timerEnd);


                            int min= (int) (timerEnd/60);
                            int sec= (int) (timerEnd%60);
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                            stopButton.setVisibility(View.GONE);
                            newRunButton.setVisibility(View.VISIBLE);
                            clicked=false;

                            textSpeed.setTextSize(15);
                            textDistance.setTextSize(15);
                            textTime.setTextSize(15);

                            textDistance.setText(String.format("DISTANCE: \n%.2f km\n%.2f m",totalDistance/1000,totalDistance));
                            textTime.setText("TIME:\n"+min+" min\n"+sec+" sec");
                            textSpeed.setText(String.format("SPEED: \n%.2f m/s",speed));

                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                            supportMapFragment.getMapAsync(RunningModeMap.this);
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);





                }
            });

            newRunButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RunningModeStopConfirm.class);
                    startActivity(intent);
                }
            });
        //finish();
       // startActivity(getIntent());

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

                                currentLocation=locationResult.getLocations().get(LatestLocationIndex);
                                    //route.add(locationResult.getLocations().get(LatestLocationIndex));
                                   route.add(currentLocation);
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

    public double getSpeed(double distance, long time) {
        double speed = 0;
        speed=distance/time;
        if(distance!=0) return speed;
        else return 0;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

                if (clicked==true) {
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    MarkerOptions startMarker = new MarkerOptions().position(latLng);

                    if (route.size() == 1) {
                        startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_marker));
                        googleMap.addMarker(startMarker);
                        startMarker.title("START");
                    }

                    if (route.size() > 1) {
                        Polyline line = googleMap.addPolyline(new PolylineOptions()
                                .add(oldLatLng, latLng)
                                .width(10)
                                .color(Color.BLUE));
                        ;
                    }

                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    googleMap.getUiSettings().setScrollGesturesEnabled(false);
                    googleMap.getUiSettings().setZoomGesturesEnabled(false);
                    oldLatLng = latLng;
                }

        if (clicked==false) { //STOP MARKER
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            if(currentLocation!=null){
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions stopMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker));
            googleMap.addMarker(stopMarker);

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));}
        }







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
