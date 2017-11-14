package com.me.esztertoth.vetclinicapp.dagger.module;

import com.me.esztertoth.vetclinicapp.rest.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    public NetModule() {

    }

    @Provides
    @Singleton
    ApiClient provideApiClient() {
        return new ApiClient();
    }

}
