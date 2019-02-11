package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.PlayActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = PlaySongViewModule.class)
public interface PlaySongViewComponent {
    void inject(PlayActivity playActivity);
    // void inject(PlayActivity playActivity);
}
