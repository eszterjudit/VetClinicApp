package com.me.esztertoth.vetclinicapp.rest;

import android.text.TextUtils;

import javax.inject.Inject;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private AuthenticationApiInterface authenticationApiInterface = null;
    private String baseUrl = "https://vet-clinic.herokuapp.com/";

    @Inject
    public ApiClient() {
    }

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    public AuthenticationApiInterface registerVet() {
        if(authenticationApiInterface == null) {
            retrofit = builder.build();
            authenticationApiInterface = retrofit.create(AuthenticationApiInterface.class);
        }

        return authenticationApiInterface;
    }

}
