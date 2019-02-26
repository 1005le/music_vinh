package com.example.music_vinh.injection;

import com.example.music_vinh.MusicApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class})
public interface AppComponent {
    void inject(MusicApplication musicApplication);

}
