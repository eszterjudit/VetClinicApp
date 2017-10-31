package com.me.esztertoth.vetclinicapp.dagger.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.me.esztertoth.vetclinicapp.utils.BitmapUtils;
import com.me.esztertoth.vetclinicapp.utils.FavoriteUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application application;

    public AppModule(Application mApplication) {
        this.application = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("com.me.esztertoth.vetclinicapp", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    VetClinicPreferences provideVetClinicPreferences(SharedPreferences sharedPreferences) {
        return new VetClinicPreferences(sharedPreferences);
    }

    @Provides
    @Singleton
    FavoriteUtils provideFavoriteUtils(Application application) {
        return new FavoriteUtils(application);
    }

    @Provides
    @Singleton
    BitmapUtils provideBitmapUtils() {
        return new BitmapUtils();
    }

}