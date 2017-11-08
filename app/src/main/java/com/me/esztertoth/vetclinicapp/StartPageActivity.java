package com.me.esztertoth.vetclinicapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.me.esztertoth.vetclinicapp.fragments.MapFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyClinicsFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyFavoritesFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyPetsFragment;
import com.me.esztertoth.vetclinicapp.fragments.SettingsFragment;
import com.me.esztertoth.vetclinicapp.fragments.SymptomCheckerFragment;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static int ACCESS_FINE_LOCATION = 1;

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    @Inject VetClinicPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        satisfyDependencies();

        if(prefs.getIsVet()) {
            changeNavigationItemsIfVet();
        }

        if (!isLocationPermissionGranted()) {
            RequestLocationPermission();
        }

        openMapFragment();
    }

    private void satisfyDependencies() {
        ((App) getApplication()).getAppComponent().inject(this);
    }

    private void openMapFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MapFragment mapFragment = new MapFragment();
        ft.replace(R.id.content_start_page, mapFragment);
        ft.addToBackStack(mapFragment.getTag());
        ft.commit();
    }

    private void changeNavigationItemsIfVet() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.vet_menu);
    }

    private void RequestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentToOpen = null;

        switch (id) {
            case R.id.nav_profile:
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_pets_list:
                fragmentToOpen = new MyPetsFragment();
                break;
            case R.id.nav_my_clinics:
                fragmentToOpen = new MyClinicsFragment();
                break;
            case R.id.nav_favorites:
                fragmentToOpen = new MyFavoritesFragment();
                break;
            case R.id.nav_settings:
                fragmentToOpen = new SettingsFragment();
                break;
            case R.id.nav_map:
                fragmentToOpen = new MapFragment();
                break;
            case R.id.nav_symptom_checker:
                fragmentToOpen = new SymptomCheckerFragment();
                break;
            default:
                break;
        }

        if(fragmentToOpen != null) {
            ft.replace(R.id.content_start_page, fragmentToOpen);
            ft.addToBackStack(fragmentToOpen.getTag());
            ft.commit();
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

}
