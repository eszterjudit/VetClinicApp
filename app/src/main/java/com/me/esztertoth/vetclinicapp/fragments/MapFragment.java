package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.map.LocationCallback;
import com.me.esztertoth.vetclinicapp.map.LocationConverter;
import com.me.esztertoth.vetclinicapp.map.LocationProvider;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.CancelableCallback, GoogleMap.SnapshotReadyCallback {

    @BindView(R.id.mapView) MapView mapView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;

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

    private Clinic clinicToOpen;

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

        initPlacesAutocomplete();
        initTypeSpinner();

        return view;
    }

    private void initTypeSpinner() {
        typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, PetType.values()));
    }

    private void initPlacesAutocomplete() {
        PlaceAutocompleteFragment places = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        places.setFilter(typeFilter);
        places.setHint("City");
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
            }

            @Override
            public void onError(Status status) {
            }
        });
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
        clinicToOpen = markers.get(marker.getId());
        marker.hideInfoWindow();
        adjustCameraAndTakeSnapshot(marker.getPosition(), 15);
    }

    private void adjustCameraAndTakeSnapshot(final LatLng mapPosition, final float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mapPosition).zoom(zoomLevel).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), this);
    }


    @Override
    public void onFinish() {
        map.snapshot(this);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onSnapshotReady(Bitmap snapshot) {
        Intent i = new Intent(getActivity(), ClinicDetailsActivity.class);
        i.putExtra("clinic", clinicToOpen);
        snapshot = resizeBitmap(snapshot, 800);
        i.putExtra("snapshot", convertBitmapToByteArray(snapshot));
        startActivity(i);
    }

    private byte[] convertBitmapToByteArray(Bitmap snapshot) {
        ByteArrayOutputStream outpuStream = new ByteArrayOutputStream();
        snapshot.compress(Bitmap.CompressFormat.PNG, 100, outpuStream);
        return outpuStream.toByteArray();
    }

    public Bitmap resizeBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}