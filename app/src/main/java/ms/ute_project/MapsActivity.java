package ms.ute_project;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity {


    private static final String TAG_GEOMETRY_COORDINATES_LAT = "lat";
    private static final String TAG_GEOMETRY_COORDINATES_LON = "lon";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        // Receiving the Data
        int flag = i.getFlags();
        if (flag != 0) {
            float lat = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LAT));
            float lon = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LON));
            setUpMapIfNeeded(lon, lat, flag);
        } else {
            String[] cords = i.getStringArrayExtra("cords");
            setUpMapIfNeeded(cords);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        // Receiving the Data
        int flag = i.getFlags();
        if (flag != 0) {
            float lat = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LAT));
            float lon = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LON));
            setUpMapIfNeeded(lon, lat, flag);
        } else {
            String[] cords = i.getStringArrayExtra("cords");
            setUpMapIfNeeded(cords);
        }

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * * call {@link } once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded(float lon, float lat, int flag) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(mMap, lon, lat, flag);
            }
        }
    }

    private void setUpMapIfNeeded(String[] cordinate) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(mMap, cordinate);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(GoogleMap map, float lon, float lat, int flag) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lon), 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        switch (flag) {

            case 0: {
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.metro))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lon)));
                break;
            }
            case 1: {
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bikes))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lon)));
                break;
            }
            case 2: {
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.parkandride))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(new LatLng(lat, lon)));
                break;
            }


        }
    }

    private void setUpMap(GoogleMap map, String[] cordinate) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(52.233333, 21.016667), 160));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.

        for (int index = 0; index < cordinate.length; index ++) {

            Float lat = Float.parseFloat((cordinate[0].substring(5,9)));
            Float lon = Float.parseFloat(cordinate[0].substring(19));


            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.metro))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .position(new LatLng(lat, lon)));

        }
    }


}

