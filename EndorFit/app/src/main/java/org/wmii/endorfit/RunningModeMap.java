package org.wmii.endorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Vector;

public class RunningModeMap extends FragmentActivity implements OnMapReadyCallback {

    TextView textView;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_map);

        textView = (TextView) findViewById(R.id.textView3) ;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            fetchLastLocation();


    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()+" "+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(RunningModeMap.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                MarkerOptions startMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_marker));
                googleMap.addMarker(startMarker);
                startMarker.title("START");

                        
            textView.setText("AKTUALNE POLOZENIE: "+currentLocation.getLatitude()+", "+currentLocation.getLongitude());


        LatLng End = latLng;
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(End));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(End, 10));

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
