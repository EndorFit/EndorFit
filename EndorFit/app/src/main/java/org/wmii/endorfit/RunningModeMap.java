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
import android.widget.ArrayAdapter;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Vector;

public class RunningModeMap extends FragmentActivity implements OnMapReadyCallback {

    protected Vector<Location> route = new Vector<Location>();


    long timer=0;
    long RunTime=0;

    boolean clicked=false;
    Button startButton;
    Button stopButton;
    Button newRunButton;
    Button saveRunButton;

    TextView textSpeed;
    TextView textDistance;
    TextView textTime;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    LatLng oldLatLng;

    pl.droidsonroids.gif.GifImageView gifIMG;

    FirebaseDatabase database;
    DatabaseReference exerciseRef;
    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_map);




        startButton =(Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        newRunButton = (Button) findViewById(R.id.newRunButton);
        saveRunButton = (Button) findViewById(R.id.saveRunButton);
        stopButton.setEnabled(false);
        stopButton.setVisibility(View.GONE);
        gifIMG= (pl.droidsonroids.gif.GifImageView) findViewById(R.id.gifIMG);

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
                    gifIMG.setVisibility(View.GONE);


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
                            RunTime=timerEnd;
                            double totalDistance=getTotalDistance();
                            double speed=getSpeed(totalDistance,timerEnd);



                            int min= (int) (timerEnd/60);
                            int sec= (int) (timerEnd%60);

                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                            stopButton.setVisibility(View.GONE);
                            newRunButton.setVisibility(View.VISIBLE);
                            saveRunButton.setVisibility(View.VISIBLE);
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
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(RunningModeMap.this);
                        builder2.setCancelable(false);
                        builder2.setIcon(R.drawable.warning_icon_default);
                        builder2.setMessage("Are you sure to finish this run and make another?");
                        builder2.setTitle("Please, Confirm...");
                        builder2.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), RunningModeMap.class);
                                finish();
                                startActivity(intent);
                            }
                        });
                    final AlertDialog dialog = builder2.create();
                    dialog.show();

                    dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                    }
                });


            saveRunButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            user = firebaseAuth.getCurrentUser();
                            if (user == null) {
                                Intent intent = new Intent(RunningModeMap.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    user = mAuth.getCurrentUser();

                    initializeObjects();
                }
            });




    }

    private void initializeObjects() {

        String data = java.text.DateFormat.getDateTimeInstance().format(new Date());


        database = FirebaseDatabase.getInstance();
       // exerciseRef = database.getReference("users/" + user.getUid() + "/ukonczoneBiegi/"+data);//stara sciezka


        String nazwa_planu = getIntent().getStringExtra("EXTRA_WORKOUT_KEY");
        exerciseRef = database.getReference("users/" + user.getUid() + "/completed/"+data+"/"+nazwa_planu);

        double totalDistance=getTotalDistance();


        //(String name, String type, double distance, double time, Vector<Location> route)

        exerciseRef.setValue(new Exercise("bieganie", "running", totalDistance, RunTime, route)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RunningModeMap.this, "Run saved", Toast.LENGTH_SHORT).show();
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
