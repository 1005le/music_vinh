package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.ArtistFragment;
import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.SongFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ArtistViewModule.class)
public interface ArtistViewComponent {

    void inject(MainActivity activity);
    void inject(ArtistFragment artistFragment);
}
