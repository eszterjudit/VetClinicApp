package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.AddNewPetActivity;
import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.DeletePetCallback;
import com.me.esztertoth.vetclinicapp.adapters.PetsListAdapter;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
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

public class MyPetsFragment extends Fragment implements DeletePetCallback {

    @BindView(R.id.pets_list_recyclerview) RecyclerView petsListRecyclerView;
    @BindView(R.id.addNewPetButton) FloatingActionButton addNewPetButton;
    @BindView(R.id.no_pets_message) TextView noPetsMessage;

    @Inject
    ApiClient apiClient;

    private List<Pet> pets;
    private Subscription subscription;
    private PetsListAdapter petsListAdapter;
    private PetOwnerApiInterface petOwnerApiInterface;

    private String token;
    private long userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets_list, container, false);
        ButterKnife.bind(this, view);

        pets = new ArrayList();

        token = VetClinicPreferences.getSessionToken(getContext());
        userId = VetClinicPreferences.getUserId(getContext());
        petOwnerApiInterface = apiClient.createService(PetOwnerApiInterface.class, token);

        petsListAdapter = new PetsListAdapter(getContext(), pets, this);
        petsListRecyclerView.setAdapter(petsListAdapter);
        petsListRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        petsListRecyclerView.setLayoutManager(llm);

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
        pets.clear();
        getOwnerAllPets();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getOwnerAllPets() {
        subscription = petOwnerApiInterface.getPetOwnerAllPets(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public final void onCompleted() {
                        if(!pets.isEmpty()) {
                            showView(noPetsMessage, false);
                            showView(petsListRecyclerView, true);
                            petsListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public final void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public final void onNext(List<Pet> response) {
                        for(Pet pet : response) {
                            pets.add(pet);
                        }
                    }
                });
    }

    @Override
    public void deletePet(long petId) {
        Pet petToDelete = pets.stream().filter(pet -> pet.getId() == petId).findFirst().get();
        petOwnerApiInterface.deletePet(token, userId, petId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    pets.remove(petToDelete);
                    petsListAdapter.notifyDataSetChanged();
                    if(pets.isEmpty()) {
                        showView(noPetsMessage, true);
                        showView(petsListRecyclerView, false);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    @OnClick(R.id.addNewPetButton)
    public void openAddNewPetFragment() {
        Intent i = new Intent(getActivity(), AddNewPetActivity.class);
        startActivity(i);
    }
}
