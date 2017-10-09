package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetOwner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiInterface {

    @GET("pet/{petId}")
    Observable<Pet> getPet(@Path("petId") Long petId);

    @GET("petOwner/{petOwnerId}/pets")
    Observable<List<Pet>> getPetOwnerAllPets(@Path("petOwnerId") Long petOwnerId);

}
