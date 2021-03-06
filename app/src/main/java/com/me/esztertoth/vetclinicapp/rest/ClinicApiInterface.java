package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.PetType;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ClinicApiInterface {

    @GET("/clinic")
    Observable<List<Clinic>> getAllClinics(@Header("x-auth-token") String token, @Query("city") String city, @Query("petType") PetType petType, @Query("onlyOpen") boolean onlyOpen);

    @POST("clinic/{clinicId}/addVet")
    Call<Clinic> addVetToClinic(@Header("x-auth-token") String token, @Path("clinicId") Long clinicId, @Body Long vetId);

    @POST("clinic/{vetId}")
    Call<ResponseBody> addClinic(@Header("x-auth-token") String token, @Path("vetId") Long vetId, @Body Clinic clinic);

    @POST("clinic/{clinicId}/removeVet")
    Call<Clinic> removeVetFromClinic(@Header("x-auth-token") String token, @Path("clinicId") Long clinicId, @Body Long vetId);

}
