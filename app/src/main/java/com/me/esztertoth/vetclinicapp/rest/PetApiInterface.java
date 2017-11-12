package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Pet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PetApiInterface {

    @DELETE("/pet/{petId}")
    Call<Void> deletePet(@Header("x-auth-token") String token, @Path("petId") long petId);

    @PUT("/pet/{petId}")
    Call<ResponseBody> updatePet(@Header("x-auth-token") String token, @Path("petId") long petId, @Body Pet pet);

}
