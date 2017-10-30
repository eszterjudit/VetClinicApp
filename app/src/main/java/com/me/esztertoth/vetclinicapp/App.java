package com.me.esztertoth.vetclinicapp;

import android.app.Application;

import com.me.esztertoth.vetclinicapp.dagger.component.DaggerNetComponent;
import com.me.esztertoth.vetclinicapp.dagger.component.NetComponent;
import com.me.esztertoth.vetclinicapp.dagger.module.AppModule;
import com.me.esztertoth.vetclinicapp.dagger.module.NetModule;

public class App extends Application {

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

}
