package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface VetApiInterface {

    @GET("/vet/{vetId}")
    Observable<User> getVet(@Header("x-auth-token") String token, @Path("vetId") Long vetId);

    @PUT("/vet/{vetId}")
    Call<ResponseBody> updateVet(@Header("x-auth-token") String token, @Path("vetId") Long vetId, @Body User user);

    @GET("/vet/{vetId}/clinics")
    Observable<List<Clinic>> getVetAllClinics(@Header("x-auth-token") String token, @Path("vetId") Long vetId);

}