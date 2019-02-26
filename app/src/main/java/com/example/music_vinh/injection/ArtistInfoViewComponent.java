package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistInfoActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ArtistInfoViewModule.class)
public interface ArtistInfoViewComponent {
    void inject(ArtistInfoActivity artistInfoActivity);
}
