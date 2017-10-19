package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetOwner;
import com.me.esztertoth.vetclinicapp.model.Vet;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiInterface {

    @GET("pet/{petId}")
    Observable<Pet> getPet(@Header("x-auth-token") String token, @Path("petId") Long petId);

    @GET("petOwner/{petOwnerId}/pets")
    Observable<List<Pet>> getPetOwnerAllPets(@Header("x-auth-token") String token, @Path("petOwnerId") Long petOwnerId);

    @GET("clinic/")
    Observable<List<Clinic>> getAllClinics(@Header("x-auth-token") String token);

    @GET("clinic/{clinicId}/vets")
    Observable<List<Vet>> getClinicAllVets(@Header("x-auth-token") String token, @Path("clinicId") Long clinicId);

    @GET("auth")
    Call<Map<String, Object>> getToken();

}
