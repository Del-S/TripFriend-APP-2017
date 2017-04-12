package com.tripfriend.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripfriend.R;
import com.tripfriend.model.Friend;
import com.tripfriend.model.configuration.Configuration;
import com.tripfriend.utils.FetchUrl;
import com.tripfriend.utils.MapDrawer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FindFriendS1Fragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        MapDrawer,
        LocationListener {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private FragmentActivity c;
    private LatLng currentPosition;
    private LatLng selectedPosition;
    private Polyline line;
    private Marker lMarker;
    private final int ZOOM_IN_LENGTH = 2000;
    public static final int PERMISSION_LOCATION_RC = 1;
    private final String TAG = "FindFriendS1Fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_friend, container, false);
        c = getActivity();

        mMapView = (MapView) view.findViewById(R.id.fff_map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(c.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();

        return view;

    }

    /**
     * Creates location request to get GPS location updates
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);  // 10 000 milisec - 10 sec
        mLocationRequest.setFastestInterval(5000);  // 5 000 milisec - 5 sec
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        displayPositionMarker(location.getLatitude(), location.getLongitude());
        Toast.makeText(c, "onLocationChanged - lat: " + location.getLatitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                displayPositionMarker(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                displayFriendMarkers(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mGoogleMap.setOnMarkerClickListener(this);

                // Start location updates after Zoom in animation ends
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        }
                    }
                }, ZOOM_IN_LENGTH);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        checkLocationPermission();
    }

    /**
     * Check if the app can access GPS (Fine Location)
     */
    public void checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // You don't have the permission you need to request it
            ActivityCompat.requestPermissions(c, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_LOCATION_RC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // You don't have the permission you need to request it
            ActivityCompat.requestPermissions(c, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_LOCATION_RC);
        }
    }

    /**
     * Displays current user location
     *
     * @param latitude
     * @param longitude
     */
    private void displayPositionMarker(double latitude, double longitude) {
        currentPosition = new LatLng(latitude, longitude);
        if(lMarker == null) {
            lMarker = mGoogleMap.addMarker(new MarkerOptions().position(currentPosition));
            lMarker.setTag(-1);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), ZOOM_IN_LENGTH, null);
        } else {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition), 200, null);
            lMarker.setPosition(currentPosition);
        }
    }

    /**
     * Displays friend in random location around current location (for now)
     *
     * @param latitude of current location
     * @param longitude of current location
     */
    private void displayFriendMarkers(double latitude, double longitude) {
        Configuration config = Configuration.getInstance();
        List<Friend> friends = config.getFriends();

        if(friends != null) {
            for (Friend f : friends) {
                double rLat = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
                double rLong = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
                LatLng position = new LatLng(latitude + rLat, longitude + rLong);
                Marker fMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title("Name: " + f.getName())
                        .snippet("Languages: " + f.getLanguages().size())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person_map_orange)));
                fMarker.setTag(f.getId());
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int idFriend = (int) marker.getTag();
        if(idFriend >= 0) {
            // TODO: send notification to the Friend device and let him confirm the tour
            // - for now assuming confirmation

            marker.showInfoWindow();

            // Getting URL to the Google Directions API
            selectedPosition = marker.getPosition();
            String url = getUrl(currentPosition, selectedPosition);
            FetchUrl fetchUrl = new FetchUrl(this);

            // Start downloading json data from Google Directions API
            fetchUrl.execute(url);

        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Starts checking if the location changed
     */
    protected void startLocationUpdates() {
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stops location checking
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Generates url for Google Directions API
     *
     * @param origin LatLng of starting point
     * @param dest LatLng of ending point
     * @return url to call
     */
    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    public void drawLine(PolylineOptions lineOptions) {
        if(line != null) {
            line.remove();
        }

        // Ajust line styles
        lineOptions.width(10);
        lineOptions.color(ContextCompat.getColor(c, R.color.colorAccent));

        line = mGoogleMap.addPolyline(lineOptions);
    }
}
