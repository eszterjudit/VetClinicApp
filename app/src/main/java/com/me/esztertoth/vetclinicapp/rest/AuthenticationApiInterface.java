package com.me.esztertoth.vetclinicapp.rest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthenticationApiInterface {

    @GET("auth")
    Call<Map<String, Object>> getToken();

}