package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.model.Vet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicDetailsFragment extends Fragment {

    @BindView(R.id.clinic_address_tv)
    TextView addressTextView;
    @BindView(R.id.clinic_opening_hours_tv)
    TextView openingHoursTextView;
    @BindView(R.id.clinic_speciality_tv)
    TextView specialitiesTextView;
    @BindView(R.id.clinic_vets_tv)
    TextView doctorsTextView;

    private Clinic clinic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
           clinic = (Clinic) bundle.getSerializable("clinic");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_details, container, false);
        ButterKnife.bind(this, view);

        setNameOnToolbar();
        setDetails();

        return view;
    }

    private void setDetails() {
        addressTextView.setText(clinic.getAddress().toString());
        openingHoursTextView.setText(clinic.getOpeningHour() + " - " + clinic.getClosingHour());
        specialitiesTextView.setText(createListOfSpecialities());
        doctorsTextView.setText(createListOfVetNames());
    }

    private String createListOfVetNames() {
        StringBuilder builder = new StringBuilder();
        for (Vet vet : clinic.getVetList()) {
            builder.append(vet.getFirstName() + " " + vet.getLastName() + "\n");
        }
        return builder.toString();
    }

    private String createListOfSpecialities() {
        List<PetType> specialities = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (Vet vet : clinic.getVetList()) {
            for(PetType petType : vet.getSpeciality()) {
                if(!specialities.contains(petType)) {
                    specialities.add(petType);
                    builder.append(petType + "\n");
                }
            }
        }
        return builder.toString();
    }

    private void setNameOnToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(clinic.getName());
    }

}
