package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.PetOwner;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileContentFragment extends Fragment {

    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.street)
    TextView street;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.country)
    TextView country;
    @BindView(R.id.zip)
    TextView zip;
    @BindView(R.id.vet_specialities_conatiner)
    RelativeLayout specialitiesContainer;
    @BindView(R.id.vet_specialities)
    TextView vetSpecialities;

    private static final String USER = "user";

    private PetOwner petOwner;
    private Vet vet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_content, container, false);
        ButterKnife.bind(this, view);

        boolean isVet = VetClinicPreferences.getIsVet(getContext());

        if(isVet){
            vet = (Vet) getArguments().getSerializable(USER);
            setVetDetails();
        } else {
            petOwner = (PetOwner) getArguments().getSerializable(USER);
            setPetOwnerDetails();
        }

        return view;
    }

    private void setPetOwnerDetails() {
        if(!TextUtils.isEmpty(petOwner.getPhone()))
            phone.setText(petOwner.getPhone());
        if(!TextUtils.isEmpty(petOwner.getEmail()))
            email.setText(petOwner.getEmail());
        if(petOwner.getAddress() != null) {
            if (!TextUtils.isEmpty(petOwner.getAddress().getStreet()))
                street.setText(petOwner.getAddress().getStreet());
            if (!TextUtils.isEmpty(petOwner.getAddress().getCity()))
                city.setText(petOwner.getAddress().getCity());
            if (!TextUtils.isEmpty(petOwner.getAddress().getZip()))
                zip.setText(petOwner.getAddress().getZip());
            if (!TextUtils.isEmpty(petOwner.getAddress().getCountry()))
                country.setText(petOwner.getAddress().getCountry());
        }
    }

    private void setVetDetails() {
        specialitiesContainer.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(vet.getPhone()))
            phone.setText(vet.getPhone());
        if(!TextUtils.isEmpty(vet.getEmail()))
            email.setText(vet.getEmail());
        if(vet.getAddress() != null) {
            if (!TextUtils.isEmpty(vet.getAddress().getStreet()))
                street.setText(vet.getAddress().getStreet());
            if (!TextUtils.isEmpty(vet.getAddress().getCity()))
                city.setText(vet.getAddress().getCity());
            if (!TextUtils.isEmpty(vet.getAddress().getZip()))
                zip.setText(vet.getAddress().getZip());
            if (!TextUtils.isEmpty(vet.getAddress().getCountry()))
                country.setText(vet.getAddress().getCountry());
        }
        if(!vet.getSpeciality().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (PetType petType : vet.getSpeciality())
            {
                sb.append(petType);
                sb.append("\n");
            }
            vetSpecialities.setText(sb.toString());
        }
    }

}
