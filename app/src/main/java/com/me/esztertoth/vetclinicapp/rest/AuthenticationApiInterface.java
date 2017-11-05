package com.me.esztertoth.vetclinicapp.rest;

import com.me.esztertoth.vetclinicapp.model.UserDTO;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthenticationApiInterface {

    @GET("auth")
    Call<Map<String, Object>> getToken();

    @POST("registerVet")
    Call<ResponseBody> registerVet(@Body UserDTO user);

    @POST("registerPetOwner")
    Call<ResponseBody> registerPetOwner(@Body UserDTO user);

}