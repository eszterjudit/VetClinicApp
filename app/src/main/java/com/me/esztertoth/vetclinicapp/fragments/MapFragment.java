package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.map.LocationCallback;
import com.me.esztertoth.vetclinicapp.map.LocationConverter;
import com.me.esztertoth.vetclinicapp.map.LocationProvider;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ClinicApiInterface;
import com.me.esztertoth.vetclinicapp.utils.BitmapUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.CancelableCallback, GoogleMap.SnapshotReadyCallback {

    private static final int MAP_ZOOM_LEVEL = 15;
    private static final int DESIRED_SNAPSHOT_SIZE = 800;
    private static final String CLINIC_NAME = "clinic";
    private static final String SNAPSHOT_NAME = "snapshot";
    private static final String SPINNER_DEFAULT = "All pets";

    @BindView(R.id.mapView) MapView mapView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.pet_type_spinner) Spinner typeSpinner;
    @BindView(R.id.place_autocomplete_search_button) View searchButton;
    @BindView(R.id.place_autocomplete_search_input) EditText placeAutocompleteSearchInput;
    @BindView(R.id.only_show_open_clinics) AppCompatCheckBox showOpenClinicsCheckbox;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;
    @Inject BitmapUtils bitmapUtils;

    private String token;
    private Map<String, Clinic> markers;
    private Clinic clinicToOpen;

    private ClinicApiInterface clinicApiInterface;
    private Subscription subscription;

    private GoogleMap map;
    private double latitude;
    private double longitude;
    private Marker currentLocationMarker;
    private LocationProvider locationProvider;

    private String queryCity;
    private PetType queryPetType;
    private boolean queryOnlyOpen;

    private void setQueryParameters() {
        queryCity = placeAutocompleteSearchInput.getText().toString() != null ? placeAutocompleteSearchInput.getText().toString() : "";
        queryPetType = !typeSpinner.getSelectedItem().toString().equals(SPINNER_DEFAULT) ? PetType.valueOf(typeSpinner.getSelectedItem().toString().toUpperCase()) : null;
        queryOnlyOpen = showOpenClinicsCheckbox.isChecked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.map_fragment));

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        token = prefs.getSessionToken();
        clinicApiInterface = apiClient.createService(ClinicApiInterface.class, token);

        markers = new HashMap<>();

        initPlacesAutocomplete();
        initTypeSpinner();

        return view;
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    private void initTypeSpinner() {
        List<String> petTypeValues = new ArrayList<>();
        petTypeValues.add(SPINNER_DEFAULT);
        Stream.of(PetType.values()).forEach(petType -> petTypeValues.add(petType.getValue()));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, petTypeValues);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    private void initPlacesAutocomplete() {
        SupportPlaceAutocompleteFragment placeAutocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        placeAutocompleteFragment.setFilter(typeFilter);
        placeAutocompleteFragment.setHint(getString(R.string.map_autocomplete_city_hint));
        placeAutocompleteSearchInput.setTextSize(20);
        placeAutocompleteSearchInput.setTextColor(getActivity().getColor(R.color.colorPrimary));
        searchButton.setVisibility(View.GONE);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        subscription.unsubscribe();
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
        setupClinics();
    }

    @OnItemSelected(R.id.pet_type_spinner)
    @OnTextChanged(R.id.place_autocomplete_search_input)
    @OnCheckedChanged(R.id.only_show_open_clinics)
    void setupClinics() {
        setQueryParameters();
        map.clear();
        showLocationOnMap();
        fetchClinics();
    }

    private void fetchClinics() {
        subscription = clinicApiInterface.getAllClinics(token, queryCity, queryPetType, queryOnlyOpen)
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
                        for (Clinic clinic : response) {
                            addClinicToMap(clinic);
                        }

                    }
                });
    }

    private void showLocationOnMap() {
        if (!queryCity.isEmpty()) {
            zoomToSelectedCity();
        } else {
            updateMapWithCurrentLocation();
        }
    }

    private void addClinicToMap(Clinic clinic) {
        LatLng locationOfClinic = LocationConverter.getLocationFromAddress(clinic.getAddress().toString(), getContext());
        if (isClinicInSetPerimeter(locationOfClinic) || !queryCity.isEmpty()) {
            addMarker(clinic, locationOfClinic);
        }
    }

    private void addMarker(Clinic clinic, LatLng locationOfClinic) {
        Marker marker = map.addMarker(new MarkerOptions().position(locationOfClinic).title(clinic.getName()).snippet(clinic.getAddress().toString()));
        markers.put(marker.getId(), clinic);
    }

    private boolean isClinicInSetPerimeter(LatLng locationOfClinic) {
        int setPerimeter = prefs.getPerimeter();
        LatLng currentLocation = new LatLng(latitude, longitude);
        return SphericalUtil.computeDistanceBetween(currentLocation, locationOfClinic) < setPerimeter;
    }

    private void updateMapWithCurrentLocation() {
        currentLocationMarker = map.addMarker(new MarkerOptions().flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)).position(new LatLng(latitude, longitude)));
        zoomToCurrentLocation();
    }

    private void zoomToSelectedCity() {
        LatLng locationOfCity = LocationConverter.getLocationFromAddress(queryCity, getContext());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationOfCity, MAP_ZOOM_LEVEL));
    }

    private void removePreviousLocation() {
        if (currentLocationMarker != null)
            currentLocationMarker.remove();
    }

    @Override
    public void handleNewLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        removePreviousLocation();
        if (queryCity.isEmpty()) {
            updateMapWithCurrentLocation();
        }
    }

    @OnClick(R.id.fab)
    public void zoomToCurrentLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), MAP_ZOOM_LEVEL));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        clinicToOpen = markers.get(marker.getId());
        marker.hideInfoWindow();
        adjustCameraAndTakeSnapshot(marker.getPosition(), MAP_ZOOM_LEVEL);
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
        i.putExtra(CLINIC_NAME, clinicToOpen);
        snapshot = bitmapUtils.resizeBitmap(snapshot, DESIRED_SNAPSHOT_SIZE);
        i.putExtra(SNAPSHOT_NAME, bitmapUtils.convertBitmapToByteArray(snapshot));
        startActivity(i);
    }

}