package com.me.esztertoth.vetclinicapp.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    private Context context;

    public PreferencesModule(Context context) {
        this.context = context;
    }

    @Provides
    @VetClinicApplicationScope
    SharedPreferences providePreferences() {
        return context.getSharedPreferences("com.me.esztertoth.vetclinicapp", Context.MODE_PRIVATE);
    }
}
