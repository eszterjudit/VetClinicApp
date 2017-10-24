package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ClinicApiInterface;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClinicDetailsContentFragment extends Fragment {

    @BindView(R.id.clinic_address_tv)
    TextView addressTextView;
    @BindView(R.id.clinic_opening_hours_tv)
    TextView openingHoursTextView;
    @BindView(R.id.clinic_speciality_tv)
    TextView specialitiesTextView;
    @BindView(R.id.clinic_vets_tv)
    TextView doctorsTextView;
    @BindView(R.id.vet_works_here_button)
    Button vetWorksHereButton;

    private Clinic clinic;
    private User vet;

    private long userId;
    private String token;

    private ClinicApiInterface clinicApiInterface;
    private VetApiInterface vetApiInterface;

    private Subscription subscription;

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

        token = VetClinicPreferences.getSessionToken(getContext());
        userId = VetClinicPreferences.getUserId(getContext());
        clinicApiInterface = ApiClient.createService(ClinicApiInterface.class, token);
        vetApiInterface = ApiClient.createService(VetApiInterface.class, token);

        if(VetClinicPreferences.getIsVet(getContext())) {
            getVetById();

        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void showWorkHereButtonForVet() {
        vetWorksHereButton.setVisibility(View.VISIBLE);
        if(isVetAlreadyInClinic()) {
            vetWorksHereButton.setText("I don't work here");
        } else {
            vetWorksHereButton.setText("I work here");
        }
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
        Set<PetType> specialities = new HashSet<>();
        StringBuilder builder = new StringBuilder();
        for (Vet vet : clinic.getVetList()) {
            for(PetType petType : vet.getSpeciality()) {
                specialities.add(petType);
                builder.append(petType + "\n");
            }
        }
        return builder.toString();
    }

    private void setNameOnToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(clinic.getName());
    }

    @OnClick(R.id.vet_works_here_button)
    void addVetToClinic() {
        if(isVetAlreadyInClinic()) {
            removeVet();
        } else {
            postVet();
        }

    }

    private boolean isVetAlreadyInClinic() {
        return clinic.getVetList().contains(vet);
    }

    private void getVetById() {
        subscription = vetApiInterface.getVet(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public final void onCompleted() {
                        showWorkHereButtonForVet();
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(User response) {
                        vet = response;
                    }

                });
    }

    private void postVet() {
        Call<Clinic> call = clinicApiInterface.addVetToClinic(token, clinic.getId(), userId);
        call.enqueue(new Callback<Clinic>() {
            @Override
            public void onResponse(Call<Clinic> call, Response<Clinic> response) {
                if (response.isSuccessful()) {
                    clinic = response.body();
                    vetWorksHereButton.setText("I dont work here");
                }
            }

            @Override
            public void onFailure(Call<Clinic> call, Throwable t) {
            }
        });
    }

    private void removeVet() {
        Call<Clinic> call = clinicApiInterface.removeVetFromClinic(token, clinic.getId(), userId);
        call.enqueue(new Callback<Clinic>() {
            @Override
            public void onResponse(Call<Clinic> call, Response<Clinic> response) {
                if (response.isSuccessful()) {
                    clinic = response.body();
                    vetWorksHereButton.setText("I work here");
                }
            }

            @Override
            public void onFailure(Call<Clinic> call, Throwable t) {
            }
        });
    }

}
