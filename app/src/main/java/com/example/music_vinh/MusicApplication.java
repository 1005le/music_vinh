package com.example.music_vinh;

import android.app.Application;
import android.content.Context;

import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.AppModule;
import com.example.music_vinh.injection.DaggerAppComponent;

public class MusicApplication extends Application {

    private AppComponent component;

    public static MusicApplication get(Context context) {
        return (MusicApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);
    }

    public AppComponent component() {
        return component;
    }

}
