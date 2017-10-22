package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    @BindView(R.id.address_line_1)
    TextView addressLine1;
    @BindView(R.id.address_line_2)
    TextView addressLine2;

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
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
        addressLine1.setText(user.getAddress().getStreet());
        addressLine2.setText(user.getAddress().getZip() + ", " + user.getAddress().getCity());
    }

}
