package com.example.localdelivery.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.utils.PrefUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import com.example.localdelivery.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class MapsFragment extends Fragment {
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;
    private SupportMapFragment mapFragment;
    private ImageView imageViewProceed;
    private GoogleMap mMap;

    OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.e("Maps Fragment", "onMapReady: " + prefUtils.getLatitude() + "KK  " +
                    prefUtils.getLongitude());
            LatLng yourLocation = new LatLng(Double.parseDouble(prefUtils.getLatitude()),
                    Double.parseDouble(prefUtils.getLongitude()));
            googleMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 15));
            googleMap.setMyLocationEnabled(true);
            setMapLongClick(googleMap);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        prefUtils = new PrefUtils(mContext);
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        imageViewProceed = view.findViewById(R.id.imageViewProceed);
        imageViewProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , MainActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        });

        String apiKey = getString(R.string.google_maps_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(mContext, apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(mContext);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.e("Maps Fragment", "Place: " + place.getName() + ", " + place.getId());
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.e("Maps Fragment", "An error occurred: " + status);
            }
        });
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Dropped Pin")
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_BLUE))
                        .snippet(snippet));
            }
        });
    }
}