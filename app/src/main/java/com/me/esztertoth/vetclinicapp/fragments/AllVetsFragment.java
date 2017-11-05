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

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.VetsListAdapter;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllVetsFragment extends Fragment {

    @BindView(R.id.all_vets_recyclerView) RecyclerView allVetsRecyclerView;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;

    private VetApiInterface vetApiInterface;

    private String token;
    private long userId;

    private Subscription subscription;

    private List<Vet> vets;

    private VetsListAdapter vetsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_vets, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All vets");

        vets = new ArrayList<>();

        token = prefs.getSessionToken();
        userId = prefs.getUserId();

        vetApiInterface = apiClient.createService(VetApiInterface.class, token);

        vetsListAdapter = new VetsListAdapter(vets);
        allVetsRecyclerView.setAdapter(vetsListAdapter);
        allVetsRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        allVetsRecyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        vets.clear();
        getAllVets();
    }

    private void getAllVets() {
        subscription = vetApiInterface.getAllVets(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Vet>>() {
                    @Override
                    public final void onCompleted() {
                        vetsListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(List<Vet> response) {
                        for(Vet vet : response) {
                            vets.add(vet);
                        }
                    }
                });
    }

}
