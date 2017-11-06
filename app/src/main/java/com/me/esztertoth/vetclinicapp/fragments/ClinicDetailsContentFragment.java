package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.VetsListAdapter;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ClinicApiInterface;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.DialogUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClinicDetailsContentFragment extends Fragment {

    @BindView(R.id.clinic_address_tv) TextView addressTextView;
    @BindView(R.id.clinic_opening_hours_tv) TextView openingHoursTextView;
    @BindView(R.id.clinic_speciality_tv) TextView specialitiesTextView;
    @BindView(R.id.vet_works_here_button) Button vetWorksHereButton;
    @BindView(R.id.all_vets_recyclerView) RecyclerView allVetsRecyclerView;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;
    @Inject DialogUtils dialogUtils;

    private static final String CLINIC_NAME = "clinic";

    private VetsListAdapter vetsListAdapter;

    private List<Vet> vets;

    private Clinic clinic;
    private Vet vet;

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
            clinic = (Clinic) bundle.getSerializable(CLINIC_NAME);
            vets = clinic.getVetList();
        }
        satisfyDependencies();
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_details, container, false);
        ButterKnife.bind(this, view);

        setNameOnToolbar();
        setDetails();

        token = prefs.getSessionToken();
        userId = prefs.getUserId();
        clinicApiInterface = apiClient.createService(ClinicApiInterface.class, token);
        vetApiInterface = apiClient.createService(VetApiInterface.class, token);

        if (prefs.getIsVet()) {
            getVetById();
        }

        vetsListAdapter = new VetsListAdapter(vets);
        allVetsRecyclerView.setAdapter(vetsListAdapter);
        allVetsRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        allVetsRecyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void showWorkHereButtonForVet() {
        vetWorksHereButton.setVisibility(View.VISIBLE);
        if (isVetAlreadyInClinic()) {
            vetWorksHereButton.setText(getString(R.string.vet_works_here_remove_button));
        } else {
            vetWorksHereButton.setText(getString(R.string.vet_works_here_add_button));
        }
    }

    private void setDetails() {
        addressTextView.setText(clinic.getAddress().toString());
        openingHoursTextView.setText(clinic.getOpeningHour() + " - " + clinic.getClosingHour());
        listSpecialities();
    }

    private String createListOfSpecialities() {
        List<PetType> specialities = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (Vet vet : clinic.getVetList()) {
            for (PetType petType : vet.getSpeciality()) {
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

    @OnClick(R.id.vet_works_here_button)
    void addOrRemoveVet() {
        if (isVetAlreadyInClinic()) {
            removeVet();
        } else if(vet.getFirstName() == null && vet.getLastName() == null) {
            dialogUtils.showErrorMessage(getContext(), getString(R.string.no_name_set_dialog_message), getString(R.string.no_name_set_dialog_positive_button));
        } else {
            postVet();
        }

    }

    private boolean isVetAlreadyInClinic() {
        return clinic.getVetList().stream().anyMatch(vet1 -> vet.getId().equals(vet1.getId()));
    }

    private void getVetById() {
        subscription = vetApiInterface.getVet(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Vet>() {
                    @Override
                    public final void onCompleted() {
                        showWorkHereButtonForVet();
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(Vet response) {
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
                    vets.clear();
                    vets.addAll(clinic.getVetList());
                    vetsListAdapter.notifyDataSetChanged();
                    listSpecialities();
                    vetWorksHereButton.setText(getString(R.string.vet_works_here_remove_button));
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
                    vets.clear();
                    vets.addAll(clinic.getVetList());
                    vetsListAdapter.notifyDataSetChanged();
                    listSpecialities();
                    vetWorksHereButton.setText(getString(R.string.vet_works_here_add_button));
                }
            }

            @Override
            public void onFailure(Call<Clinic> call, Throwable t) {
            }
        });
    }

    private void listSpecialities() {
        specialitiesTextView.setText(createListOfSpecialities());
    }

}
