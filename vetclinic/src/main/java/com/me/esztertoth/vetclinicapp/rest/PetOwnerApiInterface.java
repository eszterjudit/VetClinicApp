package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetOwner;
import com.me.esztertoth.vetclinicapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface PetOwnerApiInterface {

    @GET("petOwner/{petOwnerId}/pets")
    Observable<List<Pet>> getPetOwnerAllPets(@Header("x-auth-token") String token, @Path("petOwnerId") Long petOwnerId);

    @POST("petOwner/{petOwnerId}/addPet/")
    Call<ResponseBody> addPet(@Header("x-auth-token") String token, @Path("petOwnerId") long petOwnerId, @Body Pet pet);

    @GET("/petOwner/{petOwnerId}")
    Observable<PetOwner> getPetOwner(@Header("x-auth-token") String token, @Path("petOwnerId") Long petOwnerId);

    @PUT("/petOwner/{petOwnerId}")
    Call<ResponseBody> updatePetOwner(@Header("x-auth-token") String token, @Path("petOwnerId") Long petOwnerId, @Body User user);

}
