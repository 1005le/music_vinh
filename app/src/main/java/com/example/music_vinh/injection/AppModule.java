package com.example.music_vinh.injection;

import android.app.Application;

import com.example.music_vinh.MusicApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MusicApplication musicApplication;

    public AppModule(MusicApplication musicApplication) {
        this.musicApplication = musicApplication;
    }
    @Provides
    @Singleton
    public Application provideApplication() {
        return musicApplication;
    }
}
