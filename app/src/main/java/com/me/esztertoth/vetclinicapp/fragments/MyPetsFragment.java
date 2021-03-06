package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.AddOrEditPetActivity;
import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.PetListAction;
import com.me.esztertoth.vetclinicapp.adapters.PetsListAdapter;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetApiInterface;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

public class MyPetsFragment extends Fragment implements PetListAction {

    @BindView(R.id.pets_list_recyclerview) RecyclerView petsListRecyclerView;
    @BindView(R.id.no_pets_message) TextView noPetsMessage;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;

    private List<Pet> pets;
    private Subscription subscription;
    private PetsListAdapter petsListAdapter;
    private PetOwnerApiInterface petOwnerApiInterface;
    private PetApiInterface petApiInterface;

    private String token;
    private long userId;

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
        View view = inflater.inflate(R.layout.fragment_pets_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_pets_fragment));

        pets = new ArrayList();

        token = prefs.getSessionToken();
        userId = prefs.getUserId();
        petOwnerApiInterface = apiClient.createService(PetOwnerApiInterface.class, token);
        petApiInterface = apiClient.createService(PetApiInterface.class, token);

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
                        Stream.of(response).forEach(pet -> pets.addAll(pet));
                    }
                });
    }

    @Override
    public void deletePet(long petId) {
        Pet petToDelete = pets.stream().filter(pet -> pet.getId() == petId).findFirst().get();
        petApiInterface.deletePet(token, petId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    pets.remove(petToDelete);
                    petsListAdapter.notifyDataSetChanged();
                    if(pets.isEmpty()) {
                        showView(noPetsMessage, true);
                        showView(petsListRecyclerView, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    @Override
    public void editPet(long petId) {
        Pet petToEdit = pets.stream().filter(pet -> pet.getId() == petId).findFirst().get();
        Intent i = new Intent(getActivity(), AddOrEditPetActivity.class);
        i.putExtra("pet", petToEdit);
        startActivity(i);
    }

    @OnClick(R.id.addNewPetButton)
    public void openAddOrEditPetFragment() {
        Intent i = new Intent(getActivity(), AddOrEditPetActivity.class);
        startActivity(i);
    }
}
