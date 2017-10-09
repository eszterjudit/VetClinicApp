package com.me.esztertoth.vetclinicapp.viewmodel;

import com.me.esztertoth.vetclinicapp.model.Pet;

import java.util.List;

public class PetListViewModel {

    private List<Pet> petList;

    public void addNewPet(Pet pet) {
        petList.add(pet);
    }

    public List<Pet> getPetList() {
        return petList;
    }

}
