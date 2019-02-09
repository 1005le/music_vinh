package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.AlbumFragment;
import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.SongFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules =AlbumViewModule.class)
public interface AlbumViewComponent {

    void inject(MainActivity activity);
    void inject(AlbumFragment albumFragment);
}
