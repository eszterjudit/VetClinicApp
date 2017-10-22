package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    private static final String USER = "user";

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_content, container, false);
        ButterKnife.bind(this, view);

        user = (User) getArguments().getSerializable(USER);
        setUserDetails();

        return view;
    }

    private void setUserDetails() {
        if(!TextUtils.isEmpty(user.getPhone()))
            phone.setText(user.getPhone());
        if(!TextUtils.isEmpty(user.getEmail()))
            email.setText(user.getEmail());
        if(!TextUtils.isEmpty(user.getAddress().getStreet()))
            street.setText(user.getAddress().getStreet());
        if(!TextUtils.isEmpty(user.getAddress().getCity()))
            city.setText(user.getAddress().getCity());
        if(!TextUtils.isEmpty(user.getAddress().getZip()))
            zip.setText(user.getAddress().getZip());
        if(!TextUtils.isEmpty(user.getAddress().getCountry()))
            country.setText(user.getAddress().getCountry());
    }

}
