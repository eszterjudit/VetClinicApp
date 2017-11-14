package com.me.esztertoth.vetclinicapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.me.esztertoth.vetclinicapp.fragments.ClinicDetailsContentFragment;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.utils.FavoriteUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClinicDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.clinic_details_bg) ImageView mapIv;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Inject VetClinicPreferences prefs;
    @Inject FavoriteUtils favoriteUtils;

    private static String CLINIC_NAME = "clinic";
    private static String SNAPSHOT_NAME = "snapshot";

    private Clinic clinic;
    private Bitmap mapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_details);
        ButterKnife.bind(this);

        clinic = (Clinic) getIntent().getSerializableExtra(CLINIC_NAME);

        byte[] b = getIntent().getExtras().getByteArray(SNAPSHOT_NAME);

        if(b != null) {
            mapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            mapImage = BitmapFactory.decodeResource(getResources(), R.drawable.clinic_bg);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        satisfyDependencies();

        initUI();
        openClinicDetailsContent();
    }

    private void satisfyDependencies() {
        ((App) getApplication()).getAppComponent().inject(this);
    }

    private void initUI() {
        mapIv.setImageBitmap(mapImage);
        hideFavoriteButtonForVet();
        if(favoriteUtils.isClinicAlreadyInFavorites(clinic)) {
            fab.setImageResource(R.drawable.ic_favorite);
        }
    }

    private void openClinicDetailsContent() {
        ClinicDetailsContentFragment clinicDetailsContentFragment = new ClinicDetailsContentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CLINIC_NAME, clinic);
        clinicDetailsContentFragment.setArguments(bundle);
        ft.replace(R.id.clinic_details_container, clinicDetailsContentFragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void favoriteOrUnfavoriteClinic() {
        if(favoriteUtils.isClinicAlreadyInFavorites(clinic)) {
            unfavorite();
        } else {
            favorite();
        }
    }

    private void favorite() {
        favoriteUtils.addFavoriteClinic(clinic);
        fab.setImageResource(R.drawable.ic_favorite);
    }

    private void unfavorite() {
        favoriteUtils.removeFavorite(clinic);
        fab.setImageResource(R.drawable.ic_favorite_border);
    }

    private void hideFavoriteButtonForVet() {
        if(prefs.getIsVet()) {
            fab.setVisibility(View.GONE);
        }
    }

}
