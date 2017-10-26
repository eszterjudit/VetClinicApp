package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.ClinicsAdapter;
import com.me.esztertoth.vetclinicapp.adapters.RecyclerViewClickListener;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyClinicsFragment extends Fragment {

    @BindView(R.id.my_clinincs_list_recyclerview) RecyclerView clinicsRecyclerView;
    @BindView(R.id.no_clinincs_message) TextView noClinicsMessage;

    private List<Clinic> clinics;

    private ClinicsAdapter clinicsAdapter;
    private VetApiInterface vetApiInterface;
    private Subscription subscription;
    private String token;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_clinincs_list, container, false);
        ButterKnife.bind(this, view);

        token = VetClinicPreferences.getSessionToken(getContext());
        userId = VetClinicPreferences.getUserId(getContext());
        vetApiInterface = ApiClient.createService(VetApiInterface.class, token);

        clinics = new ArrayList<>();

        RecyclerViewClickListener clinicClickListener = (view1, position) -> {
            openClinicDetailsFragment(position);
        };

        clinicsAdapter = new ClinicsAdapter(clinics, clinicClickListener);
        clinicsRecyclerView.setAdapter(clinicsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        clinicsRecyclerView.setLayoutManager(llm);

        return view;
    }

    private void showView(View view, boolean show) {
        if(show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clinics.clear();
        getVetAllClinics();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getVetAllClinics() {
        subscription = vetApiInterface.getVetAllClinics(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Clinic>>() {
                    @Override
                    public final void onCompleted() {
                        clinicsAdapter.notifyDataSetChanged();
                        if(!clinics.isEmpty()) {
                            showView(noClinicsMessage, false);
                            showView(clinicsRecyclerView, true);
                        } else {
                            showView(noClinicsMessage, true);
                            showView(clinicsRecyclerView, false);
                        }
                    }

                    @Override
                    public final void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public final void onNext(List<Clinic> response) {
                        for(Clinic clinic : response) {
                            clinics.add(clinic);
                        }
                    }
                });
    }

    private void openClinicDetailsFragment(int position) {
        Intent i = new Intent(getActivity(), ClinicDetailsActivity.class);
        i.putExtra("clinic", clinics.get(position));
        startActivity(i);
    }

}