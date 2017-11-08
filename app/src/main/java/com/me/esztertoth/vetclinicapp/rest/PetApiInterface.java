package com.me.esztertoth.vetclinicapp.rest;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PetApiInterface {

    @DELETE("/pet/{petId}")
    Call<Void> deletePet(@Header("x-auth-token") String token, @Path("petId") long petId);

}
