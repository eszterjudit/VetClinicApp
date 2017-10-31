package com.me.esztertoth.vetclinicapp;

import android.app.Application;

import com.me.esztertoth.vetclinicapp.dagger.component.AppComponent;
import com.me.esztertoth.vetclinicapp.dagger.component.DaggerAppComponent;
import com.me.esztertoth.vetclinicapp.dagger.component.DaggerNetComponent;
import com.me.esztertoth.vetclinicapp.dagger.component.NetComponent;
import com.me.esztertoth.vetclinicapp.dagger.module.AppModule;
import com.me.esztertoth.vetclinicapp.dagger.module.NetModule;

public class App extends Application {

    private NetComponent netComponent;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppModule appModule = new AppModule(this);

        netComponent = DaggerNetComponent.builder()
                .appModule(appModule)
                .netModule(new NetModule())
                .build();

        appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
