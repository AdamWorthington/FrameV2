package cs490.frame;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.grant.myapplication.backend.myApi.model.IAHBean;
import com.example.grant.myapplication.backend.myApi.model.ImageAttributeHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class WorldController extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleClient;

    private LocationRequest mLocationRequest;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int REQUEST_ACCESS_FINE_LOC = 200;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private int first = 0;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.world_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.cameraButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraClick();
            }
        });

        createLocationRequest();
        connectToGoogleApi();

        Intent intent = getIntent();
        displayName = intent.getStringExtra("extra_displayname");
    }

    @Override
    protected void onStart()
    {
        mGoogleClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        mGoogleClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mGoogleClient.isConnected())
            startLocationUpdates();
        else
            connectToGoogleApi();
    }

    @Override
    public void onConnected(Bundle connectionHints)
    {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int status)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.d("Connection Error", result.getErrorMessage());
    }

    private void startLocationUpdates()
    {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            setLocationListener();
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(WorldController.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't animateIn the dialog.
                            break;
                    }
                }
            });
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
    }

    private void setLocationListener()
    {
        //Check for permissions
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOC);

            return;
        }

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
                } else {
                    // permission denied. Kill the app.
                    finish();
                }
                return;
            }
        }
    }

    private void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void connectToGoogleApi()
    {
        if(mGoogleClient == null)
        {
            mGoogleClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //move camera on only the first location
        if (first == 0) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
            first++;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Map fragement setup
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(15);
        mMap.setIndoorEnabled(true);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "viewing photos", Toast.LENGTH_SHORT).show();
            }
        });

        UiSettings mMapUiSettings = mMap.getUiSettings();
        mMapUiSettings.setMyLocationButtonEnabled(true);

        enableMyLocation();

        //Get all posts from the db
        ArrayList<ImageAttributeHolder> posts = null;
        Log.e("WorldController", "here1");
        try {
            Log.e("WorldController", "here2");
            posts = new GetAllAttributes().execute().get();
            if (posts == null) {
                Log.e("WorldController", "posts is empty");
            }
        } catch (Exception e) {
            Log.e("WorldController", e.getMessage());
        }
        if (posts != null) {
            Log.e("WorldController", "making a marker");
            //add a marker for each post
            for (int i = 0; i < posts.size(); i++) {
                ImageAttributeHolder holder = posts.get(i);
                LatLng position = new LatLng(holder.getLatitude(), holder.getLatitude());
                String title = holder.getId().toString();
                String snippet = "Posted by: " + holder.getUser();
                addMapMarker(position, title, snippet);
            }
        }
    }


    public void addMapMarker(LatLng position, String title, String snippet) {
        mMap.addMarker(new MarkerOptions()
            .position(position)
            .title(title)
            //.icon()
            .snippet(snippet));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private void onCameraClick()
    {
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        Location location = null;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            connectToGoogleApi();
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
        }
        cameraIntent.putExtra("extra_location", location);
        cameraIntent.putExtra("extra_displayname", displayName);
        startActivity(cameraIntent);
    }
}
