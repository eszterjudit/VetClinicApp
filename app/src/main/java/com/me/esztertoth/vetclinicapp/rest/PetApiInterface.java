package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Pet;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

public interface PetApiInterface {

    @GET("pet/{petId}")
    Observable<Pet> getPet(@Header("x-auth-token") String token, @Path("petId") Long petId);

    @DELETE("/pet/{petId}")
    Call<Void> deletePet(@Header("x-auth-token") String token, @Path("petId") long petId);

}
