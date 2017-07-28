package com.madmen.hostr.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.madmen.hostr.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import static android.location.LocationManager.GPS_PROVIDER;

/**
 * Created by Uzezi on 5/26/2017.
 */

public class GmapFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap mGoogleMap;
    private LatLng currentLocation;
    private Location mLocation;
    private FloatingActionButton mFab;
    private LocationManager locationManager;
    private boolean gotLocation = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFab = (FloatingActionButton) container.findViewById(R.id.fab);
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_view);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(!haveLocation() && validCoords(location.getLatitude(), location.getLongitude())) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 6));
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
    }

    public boolean validCoords(double lat, double lng) {
        if(lat != 0.0 && lng != 0.0) {
            this.gotLocation = true;
            return true;
        } else return false;
    }

    public boolean haveLocation() { return this.gotLocation; }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
