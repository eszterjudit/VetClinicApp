package com.me.esztertoth.vetclinicapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.me.esztertoth.vetclinicapp.fragments.ClinicDetailsFragment;
import com.me.esztertoth.vetclinicapp.model.Clinic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.clinic_details_bg) ImageView mapIv;
    private Clinic clinic;

    private Bitmap mapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_details);
        ButterKnife.bind(this);

        clinic = (Clinic) getIntent().getSerializableExtra("clinic");
        mapImage = (Bitmap) getIntent().getParcelableExtra("snapshot");

        mapIv.setImageBitmap(mapImage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        openProfileContent();
    }

    private void openProfileContent() {
        ClinicDetailsFragment clinicDetailsFragment = new ClinicDetailsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("clinic", clinic);
        clinicDetailsFragment.setArguments(bundle);
        ft.replace(R.id.clinic_details_container, clinicDetailsFragment);
        ft.addToBackStack(clinicDetailsFragment.getTag());
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

}
