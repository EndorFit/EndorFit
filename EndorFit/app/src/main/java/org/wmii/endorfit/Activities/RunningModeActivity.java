package org.wmii.endorfit.Activities;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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

import org.wmii.endorfit.DataClasses.Exercise;
import org.wmii.endorfit.R;
import org.wmii.endorfit.DataClasses.Workout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class RunningModeActivity extends FragmentActivity implements OnMapReadyCallback {

    protected Vector<Location> route = new Vector<Location>();

    long timer = 0;
    long RunTime = 0;

    boolean clicked = false;
    Button buttonStart;
    Button buttonStop;
    Button buttonNewRun;
    Button buttonSaveRun;

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

    EditText inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_map);

        buttonStart = (Button) findViewById(R.id.startButton);
        buttonStop = (Button) findViewById(R.id.stopButton);
        buttonNewRun = (Button) findViewById(R.id.newRunButton);
        buttonSaveRun = (Button) findViewById(R.id.saveRunButton);
        buttonStop.setEnabled(false);
        buttonStop.setVisibility(View.GONE);
        gifIMG = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.gifAnimation);

        textSpeed = (TextView) findViewById(R.id.textSpeed);
        textDistance = (TextView) findViewById(R.id.textDistance);
        textTime = (TextView) findViewById(R.id.textTime);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "RUN!", Toast.LENGTH_SHORT).show();
                timer = System.currentTimeMillis();
                textDistance.setTextSize(25);
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                buttonStart.setVisibility(View.GONE);
                buttonStop.setVisibility(View.VISIBLE);
                gifIMG.setVisibility(View.GONE);

                clicked = true;

                if (ActivityCompat.checkSelfPermission(RunningModeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunningModeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RunningModeActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    return;
                } else {
                    fetchLastLocation();
                }
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RunningModeActivity.this);
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

                        long timerEnd = (System.currentTimeMillis() - timer) / 1000;
                        RunTime = timerEnd;
                        double totalDistance = getTotalDistance();
                        double speed = getSpeed(totalDistance, timerEnd);

                        int min = (int) (timerEnd / 60);
                        int sec = (int) (timerEnd % 60);

                        buttonStart.setEnabled(true);
                        buttonStop.setEnabled(false);
                        buttonStop.setVisibility(View.GONE);
                        buttonNewRun.setVisibility(View.VISIBLE);
                        buttonSaveRun.setVisibility(View.VISIBLE);
                        clicked = false;

                        textSpeed.setTextSize(15);
                        textDistance.setTextSize(15);
                        textTime.setTextSize(15);

                        textDistance.setText(String.format("DISTANCE: \n%.2f km\n%.2f m", totalDistance / 1000, totalDistance));
                        textTime.setText("TIME:\n" + min + " min\n" + sec + " sec");
                        textSpeed.setText(String.format("SPEED: \n%.2f m/s", speed));

                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                        supportMapFragment.getMapAsync(RunningModeActivity.this);
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

            }
        });

        buttonNewRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(RunningModeActivity.this);
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
                        Intent intent = new Intent(getApplicationContext(), RunningModeActivity.class);
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

        buttonSaveRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            Intent intent = new Intent(RunningModeActivity.this, MainActivity.class);
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

        final String[] nazwa_planu = {getIntent().getStringExtra("EXTRA_WORKOUT_KEY")};

        if (nazwa_planu[0] == null) {
            nazwa_planu[0] = "Running";
        }
        exerciseRef = database.getReference("users/" + user.getUid() + "/completed/" + data + "/" + nazwa_planu[0]);

        double totalDistance = getTotalDistance();

        ArrayList<Exercise> tempArray = new ArrayList<>();
        tempArray.add(new Exercise(nazwa_planu[0], "Moving", totalDistance, RunTime, route));
        Workout dbSave = new Workout("1/1", tempArray);
        exerciseRef.setValue(dbSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RunningModeActivity.this, "Run saved", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchLastLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(RunningModeActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(RunningModeActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            if (clicked == true) {
                                int LatestLocationIndex = locationResult.getLocations().size() - 1;

                                currentLocation = locationResult.getLocations().get(LatestLocationIndex);
                                //route.add(locationResult.getLocations().get(LatestLocationIndex));
                                route.add(currentLocation);
                                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                                supportMapFragment.getMapAsync(RunningModeActivity.this);

                                pause(2000);
                                fetchLastLocation();
                            }
                        }
                    }
                }, Looper.getMainLooper());
    }

    public double getTotalDistance() {
        double distance = 0;
        if (route.size() > 1) {
            for (Integer i = 0; i < route.size() - 1; i++) {
                distance += (route.get(i).distanceTo(route.get(i + 1)));
            }
        }
        return distance;
    }

    public double getSpeed(double distance, long time) {
        double speed = 0;
        speed = distance / time;
        if (distance != 0) return speed;
        else return 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (clicked == true) {
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

        if (clicked == false) { //STOP MARKER
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions stopMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_marker));
                googleMap.addMarker(stopMarker);

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    public static void pause(long timeInMilliSeconds) {
        long timestamp = System.currentTimeMillis();

        do {
            //wait
        } while (System.currentTimeMillis() < timestamp + timeInMilliSeconds);

    }

}
