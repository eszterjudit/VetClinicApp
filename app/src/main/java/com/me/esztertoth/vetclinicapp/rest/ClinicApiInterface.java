package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.model.Vet;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

public interface ClinicApiInterface {

    @GET("clinic/")
    Observable<List<Clinic>> getAllClinics(@Header("x-auth-token") String token);

    @GET("clinic/{clinicId}/vets")
    Observable<List<Vet>> getClinicAllVets(@Header("x-auth-token") String token, @Path("clinicId") Long clinicId);

}
