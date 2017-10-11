package com.me.esztertoth.vetclinicapp.rest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiInterface apiInterface = null;
    private static String BASE_URL = "http://192.168.1.5:8080/";

    public static ApiInterface provideApiClient() {
        if (apiInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            apiInterface = retrofit.create(ApiInterface.class);
        }

        return apiInterface;
    }

}
