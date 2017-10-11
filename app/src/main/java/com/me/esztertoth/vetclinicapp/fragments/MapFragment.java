package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.ProfileActivity;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.map.LocationCallback;
import com.me.esztertoth.vetclinicapp.map.LocationConverter;
import com.me.esztertoth.vetclinicapp.map.LocationProvider;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationCallback, GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.mapView) MapView mapView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private GoogleMap map;
    private double latitude;
    private double longitude;
    private Marker currentLocationMarker;
    private LocationProvider locationProvider;
    private LocationConverter locationConverter;

    private List<Clinic> clinics;
    Map<String, Clinic> markers = new HashMap<>();

    ApiInterface apiService;
    Subscription subscription;

    private void addClinicToMap(Clinic clinic) {
        LatLng locationOfClinic = locationConverter.getLocationFromAddress(clinic.getAddress().toString());
        Marker marker = map.addMarker(new MarkerOptions().position(locationOfClinic).title(clinic.getName()).snippet(clinic.getAddress().toString()));
        markers.put(marker.getId(), clinic);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationProvider = new LocationProvider(getActivity(), this);
        locationProvider.connect();
        mapView.onResume();

        locationConverter = new LocationConverter(getActivity());
        apiService = ApiClient.provideApiClient();
        clinics = new ArrayList<>();
    }

    private void getAllClinics() {
        subscription = apiService.getAllClinics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Clinic>>() {
                    @Override
                    public final void onCompleted() {
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(List<Clinic> response) {
                        for(Clinic clinic : response) {
                            clinics.add(clinic);
                            addClinicToMap(clinic);
                        }

                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationProvider.disconnect();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setOnInfoWindowClickListener(this);
        updateMapWithCurrentLocation();
        getAllClinics();
    }

    private void updateMapWithCurrentLocation() {
        currentLocationMarker = map.addMarker(new MarkerOptions().flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)).position(new LatLng(latitude,longitude)));
        zoomToCurrentLocation();
    }

    @Override
    public void handleNewLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        removePreviousLocation();
        updateMapWithCurrentLocation();
    }

    private void removePreviousLocation() {
        if(currentLocationMarker != null)
            currentLocationMarker.remove();
    }

    @OnClick(R.id.fab)
    public void zoomToCurrentLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Clinic clinicToOpen = markers.get(marker.getId());
        Intent i = new Intent(getActivity(), ClinicDetailsActivity.class);
        i.putExtra("clinic", clinicToOpen);

        startActivity(i);
    }

}