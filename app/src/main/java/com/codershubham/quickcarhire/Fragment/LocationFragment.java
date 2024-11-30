package com.codershubham.quickcarhire.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class LocationFragment extends Fragment {

    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    private LocationCallback locationCallback;
    private SharedPreferences sharedPreferences;
    String email;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        if (!InternetConnection.isInternetAvailable(requireContext())) {
            InternetConnection.showInternetDialog(requireContext());
        }

        sharedPreferences = requireActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        requestLocationPermission();
        return view;
    }

    private void requestLocationPermission() {
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                initMap(location);
            } else {
                Toast.makeText(requireContext(), "Unable to retrieve location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap(android.location.Location location) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }

    private void onLocationChanged(android.location.Location location) {
        if (location != null && googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() != null) {
                    sendLocationToServer(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    onLocationChanged(locationResult.getLastLocation());
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void sendLocationToServer(final double latitude, final double longitude) {
        String url = "https://quickcarshire.000webhostapp.com/API/map.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error sending location data", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(requireContext(), "Error sending location data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}
