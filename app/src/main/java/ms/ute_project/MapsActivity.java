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

import java.lang.String;

public class MapsActivity extends FragmentActivity {


    private static final String TAG_GEOMETRY_COORDINATES_LAT = "lat";
    private static final String TAG_GEOMETRY_COORDINATES_LON = "lon";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<String> cords = null;
    private float localization_lat;
    private float localization_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        // Receiving the Data
        int flag = i.getFlags();
        String localization_tmp = i.getStringExtra("localization");
        localization_lon = Float.parseFloat(localization_tmp.substring(0,localization_tmp.indexOf(".",4)-2));
        localization_lat = Float.parseFloat(localization_tmp.substring(localization_tmp.indexOf(".",4)-2));


        if (flag != 0) {
            float lat = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LAT));
            float lon = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LON));
            setUpMapIfNeeded(lon, lat, flag);
        } else {
            cords = new ArrayList<String>();
            cords = i.getStringArrayListExtra("cords");
            setUpMapIfNeeded(cords);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        // Receiving the Data
        int flag = i.getFlags();
        localization_lon = Float.parseFloat(i.getStringExtra("localization").substring(0,9));
        localization_lat = Float.parseFloat(i.getStringExtra("localization").substring(9));

        if (flag != 0) {
            float lat = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LAT));
            float lon = Float.parseFloat(i.getStringExtra(TAG_GEOMETRY_COORDINATES_LON));
            setUpMapIfNeeded(lon, lat, flag);
        } else {
            cords = new ArrayList<String>();
            cords = i.getStringArrayListExtra("cords");
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

    private void setUpMapIfNeeded(ArrayList<String> cordinate) {
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
                new LatLng(localization_lat, localization_lon), 15));


        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.me))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(localization_lat, localization_lon)));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        switch (flag) {

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

    private void setUpMap(GoogleMap map, ArrayList<String> cordinate) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(localization_lat, localization_lon), 15));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.


        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.me))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(localization_lat, localization_lon)));


        for (int index = 0; index < cordinate.size(); index ++) {

            Float lat = Float.parseFloat(cordinate.get(index).substring(0,9));
            Float lon = Float.parseFloat(cordinate.get(index).substring(9));


            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.metro))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .position(new LatLng(lat, lon)));

        }
    }


}

