package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.adapters.PetsListAdapter;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetsListFragment extends Fragment {

    @BindView(R.id.pets_list_recyclerview)
    RecyclerView petsListRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<Pet> pets;
    private PetsListAdapter petsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets_list, container, false);
        ButterKnife.bind(this, view);

        pets = new ArrayList();
        addDummyPets();

        petsListAdapter = new PetsListAdapter(getContext(), pets);
        petsListRecyclerView.setAdapter(petsListAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        petsListRecyclerView.setLayoutManager(llm);

        return view;
    }

    private void addDummyPets() {
        pets.add(new Pet("Simon", 10, PetType.CAT));
        pets.add(new Pet("Cat", 3, PetType.CAT));
        pets.add(new Pet("Kutya", 4, PetType.DOG));
        pets.add(new Pet("Reptile", 1, PetType.REPTILE));
        pets.add(new Pet("Reptile2", 100, PetType.REPTILE));
        pets.add(new Pet("Rodent", 2, PetType.RODENT));
        pets.add(new Pet("Bird", 1, PetType.BIRD));
    }
}
