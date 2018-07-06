package com.eariane.restorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static boolean mLocationPermisionGranted;
    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final String TAG = "MapActivity";
    String restaurantsRaw;
    Requests rq = new Requests(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermisionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermisionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();

        LatLng barranco = new LatLng(-12.1404447,-77.0233722);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barranco));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));


        // Get the restaurants
        rq.getRestaurants(new Requests.VolleyCallback() {
            @Override
            public void onSuccess(String resp) {
                restaurantsRaw = resp;
                setMarkers(restaurantsRaw);
                Log.d(TAG, "onSuccess: " + restaurantsRaw);
            }

            @Override
            public void onFailure(String error) {
                restaurantsRaw = "";
                Toast.makeText(MapActivity.this, "Error al cargar los restaurantes", Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.view_marker_details, null);
                TextView name = v.findViewById(R.id.view_marker_details_name);
                TextView tags = v.findViewById(R.id.view_marker_details_tags);
                TextView descripcion = v.findViewById(R.id.view_marker_details_description);
                TextView detalles = v.findViewById(R.id.view_marker_details_details);


                JSONObject restaurant = (JSONObject) marker.getTag();
                Log.d(TAG, "getInfoContents: " + restaurant);

                try {
                    name.setText(restaurant.getString("name"));
                    tags.setText(restaurant.getString("tags"));
                    descripcion.setText(restaurant.getString("description"));
                    String textDetalles = "";

                    if (restaurant.getBoolean("delivery")) textDetalles += "Delivery\n";
                    if (restaurant.getBoolean("kidfriendly")) textDetalles += "Kid Friendly\n";
                    if (restaurant.getBoolean("takeaway")) textDetalles += "Take away\n";
                    if (restaurant.getBoolean("vegetarian")) textDetalles += "Vegetarian";

                    detalles.setText(textDetalles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return v;
            }
        });

    }

    private void updateLocationUI() {
        if (mMap != null) {
            try {
                Log.d(TAG, "updateLocationUI: " + mLocationPermisionGranted);
                if (mLocationPermisionGranted) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermisionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void setMarkers(String rawData) {
        try {
            JSONArray restaurantes = new JSONArray(rawData);
            Log.d(TAG, "setMarkers: - # markers: " + restaurantes.length());

            for (int i = 0; i < restaurantes.length(); i++) {
                JSONObject restaurant = restaurantes.getJSONObject(i);
                double lat = restaurant.getDouble("latitude");
                double lng = restaurant.getDouble("longitude");
//                mMap.addMarker(new MarkerOptions().position(new LatLng(lan,lng))).setTitle(title);
                 mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))).setTag(restaurant);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
    }


}
