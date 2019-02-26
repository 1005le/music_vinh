package com.example.music_vinh.injection;

import com.example.music_vinh.view.impl.AlbumFragment;
import com.example.music_vinh.view.impl.AlbumInfoActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = AlbumInfoViewModule.class)
public interface AlbumInfoViewComponent {
    void inject(AlbumInfoActivity albumInfoActivity);
}
